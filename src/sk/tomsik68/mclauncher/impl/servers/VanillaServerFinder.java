package sk.tomsik68.mclauncher.impl.servers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import sk.tomsik68.mclauncher.api.servers.IFoundServer;
import sk.tomsik68.mclauncher.api.servers.IServerFinder;
import sk.tomsik68.mclauncher.impl.common.Observable;

public class VanillaServerFinder extends Observable<IFoundServer> implements IServerFinder {
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
                
            }
            String recvString = new String(packet.getData(), packet.getOffset(), packet.getLength());
            String motd = ServerStringDecoder.parseProperty(recvString, "MOTD");
            String port = ServerStringDecoder.parseProperty(recvString, "AD");
            FoundServer server = new FoundServer(this, packet.getAddress().getHostAddress()+":"+port, motd);
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
