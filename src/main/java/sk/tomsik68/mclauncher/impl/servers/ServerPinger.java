package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.servers.ServerInfo;

import java.util.concurrent.Callable;

final class ServerPinger implements Callable<ServerPingResult> {
    private final ServerInfo server;

    public ServerPinger(ServerInfo server) {
        this.server = server;
    }

    public ServerPingResult call() throws Exception {
        ServerPingResult result = null;

        return null;
    }
}
