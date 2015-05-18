package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.servers.ServerInfo;

import javax.net.SocketFactory;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketImplFactory;
import java.util.concurrent.Callable;

final class ServerPinger implements Callable<ServerPingResult> {
    private final ServerPingPacketFactory pingPacketFactory;
    private final ServerInfo server;

    public ServerPinger(ServerInfo server, ServerPingPacketFactory pingPacketFactory) {
        this.server = server;
        this.pingPacketFactory = pingPacketFactory;
    }

    public ServerPingResult call() throws Exception {
        ServerPingResult result = null;
        // open a connection
        InetSocketAddress addr = InetSocketAddress.createUnresolved(server.getIP(), server.getPort());
        Socket socket = SocketFactory.getDefault().createSocket();
        socket.connect(addr);
        // send data
        OutputStream os = socket.getOutputStream();
        os.write();
        // wait for data
        // receive data


        return null;
    }
}
