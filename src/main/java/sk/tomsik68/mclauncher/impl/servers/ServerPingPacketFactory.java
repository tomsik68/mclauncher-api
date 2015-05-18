package sk.tomsik68.mclauncher.impl.servers;

abstract class ServerPingPacketFactory {
    abstract byte[] createPingPacket();
}
