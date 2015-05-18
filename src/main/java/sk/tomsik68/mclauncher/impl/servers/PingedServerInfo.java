package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.servers.ServerInfo;

public final class PingedServerInfo extends ServerInfo {

    public PingedServerInfo(String ip, String name, String icon, int port) {
        super(ip, name, icon, port);
    }
}
