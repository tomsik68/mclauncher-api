package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.servers.ServerInfo;

abstract class ServerPingPacketFactory {
    abstract byte[] createPingPacket(ServerInfo serverInfo);
}
