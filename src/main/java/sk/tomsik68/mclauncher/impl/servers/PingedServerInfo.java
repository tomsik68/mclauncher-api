package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.servers.ServerInfo;

/**
 * Information about a server we've pinged
 */
public abstract class PingedServerInfo extends ServerInfo {

    PingedServerInfo(String ip, String name, String icon, int port) {
        super(ip, name, icon, port);
    }

    public abstract int getOnlinePlayers();

    public abstract int getMaxPlayers();

    public abstract String getMessage();

    public abstract String getVersionId();
}
