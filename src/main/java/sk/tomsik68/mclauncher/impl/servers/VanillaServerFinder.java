package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.servers.FoundServerInfo;
import sk.tomsik68.mclauncher.api.servers.IServerFinder;
import sk.tomsik68.mclauncher.impl.common.Observable;

import java.io.IOException;
import java.net.*;

public class VanillaServerFinder extends Observable<FoundServerInfo> implements IServerFinder {
    private static final String SOCKET_GROUP_ADDRESS = "224.0.2.60";
    private Thread thread;
    private MulticastSocket socket;
    private InetAddress broadcastAddress;

    public VanillaServerFinder() throws UnknownHostException {
        broadcastAddress = InetAddress.getByName(SOCKET_GROUP_ADDRESS);
    }

    @Override
    public void run() {
        MulticastSocket socket = null;
        byte[] buffer = new byte[1024];
        try {
            socket = new MulticastSocket(4445);
            socket.setSoTimeout(5000);
            socket.joinGroup(this.broadcastAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (socket != null && isActive()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException ign) {
                continue;
            } catch (Exception e) {
                // TODO: add option to handle this error!
            }
            String recvString = new String(packet.getData(), packet.getOffset(), packet.getLength());
            String motd = ServerStringDecoder.parseProperty(recvString, "MOTD");
            String port = ServerStringDecoder.parseProperty(recvString, "AD");
            FoundServer server = new FoundServer(this, packet.getAddress().getHostAddress() + ":" + port, motd);
            server.getInformation().put("motd", motd);
            server.getInformation().put("address", packet.getAddress().getHostAddress() + ":" + port);
            server.getInformation().put("discoveryData", packet.getData());
            server.getInformation().put("packet", packet);
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
