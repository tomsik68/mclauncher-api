package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.servers.ServerInfo;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * This is implementation of http://wiki.vg/Server_List_Ping
 */
final class Protocol47PingPacketFactory extends ServerPingPacketFactory {

    private static int readVarInt(DataInputStream in) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }

    private static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }

            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    private static void writeRequestPacket(DataOutputStream dos, ServerInfo serverInfo) throws IOException {
        // packet ID
        dos.writeByte(0);
        // protocol version
        writeVarInt(dos, 47);
        // server address
        dos.writeUTF(serverInfo.getIP());
        // server port
        dos.writeShort(serverInfo.getPort());
        // next state = 1 (status)
        writeVarInt(dos, 1);

        // request
        dos.writeByte(0);
    }

    @Override
    byte[] createPingPacket(ServerInfo serverInfo) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            writeRequestPacket(dos, serverInfo);

            baos.flush();
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }
}
