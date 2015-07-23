package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.servers.FoundServerInfo;
import sk.tomsik68.mclauncher.api.servers.IServerFinder;
import sk.tomsik68.mclauncher.impl.common.Observable;

import java.io.IOException;
import java.net.*;

/**
 * Wrapper for thread that listens for servers on LAN.
 */
public final class VanillaServerFinder extends Observable<FoundServerInfo> implements IServerFinder {
    private static final String SOCKET_GROUP_ADDRESS = "224.0.2.60";
    private Thread thread;
    private final InetAddress broadcastAddress;

    public VanillaServerFinder() throws UnknownHostException {
        broadcastAddress = InetAddress.getByName(SOCKET_GROUP_ADDRESS);
    }

    @Override
    public void run() {
        MCLauncherAPI.log.fine("Starting server finder...");
        // create socket
        MulticastSocket socket = null;
        byte[] buffer = new byte[1024];
        try {
            // assign it to group
            socket = new MulticastSocket(4445);
            socket.setSoTimeout(5000);
            socket.joinGroup(this.broadcastAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // we will use single builder to build all FoundServerInfo objects
        final FoundServerInfoBuilder builder = new FoundServerInfoBuilder();
        builder.finder(this);
        while (socket != null && isActive()) {
            // try to receive a packet
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException ign) {
                continue;
            } catch (Exception e) {
                // TODO: add option to handle this error!
            }
            // if packet was received successfully,

            String recvString = new String(packet.getData(), packet.getOffset(), packet.getLength());
            String motd = ServerStringDecoder.parseProperty(recvString, "MOTD");
            Integer port = Integer.parseInt(ServerStringDecoder.parseProperty(recvString, "AD"));
            MCLauncherAPI.log.finer("Discovered server: '".concat(recvString).concat("'"));
            // we can construct FoundServerInfo using given information
            builder.motd(motd).port(port).ip(packet.getAddress().getHostAddress());
            builder.property("recvString", recvString);
            FoundServerInfo server = builder.build();
            // and notify all observers about it
            notifyObservers(server);
        }

    }

    @Override
    public boolean isActive() {
        return thread.isAlive();
    }

    @Override
    public void startFinding() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void stop() {
        thread.interrupt();
    }

}
