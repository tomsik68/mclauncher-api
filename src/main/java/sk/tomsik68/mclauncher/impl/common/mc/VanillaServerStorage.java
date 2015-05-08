package sk.tomsik68.mclauncher.impl.common.mc;

import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.TagType;
import com.flowpowered.nbt.stream.NBTInputStream;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.servers.ServerInfo;
import sk.tomsik68.mclauncher.api.servers.IServerStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.List;

/**
 * Server storage implementation used by the default launcher
 */
public class VanillaServerStorage implements IServerStorage {
    private final File file;
    private static final int DEFAULT_PORT = 25565;

    public VanillaServerStorage(File file){
        this.file = file;
    }


    private static ServerInfo createServerFromTag(CompoundMap compound){
        String ipString, ip, name;
        int port;
        ipString = compound.get("ip").getValue().toString();
        name = compound.get("name").getValue().toString();

        if(ipString.contains(":")){
            ip = ipString.split(":")[0];
            try {
                port = Integer.parseInt(ipString.split(":")[0]);
            } catch(NumberFormatException exce){
                MCLauncherAPI.log.severe("Bad port number format: '"+ipString.split(":")[0]+"' ");
                exce.printStackTrace();
                return null;
            }
        } else{
            ip = ipString;
            port = DEFAULT_PORT;
        }

        ServerInfo result = new ServerInfo(ip, name, port);
        return result;
    }


    public ServerInfo[] loadServers() throws Exception{
        final FileInputStream fis = new FileInputStream(file);
        NBTInputStream nbtIs = new NBTInputStream(fis);
        // the cast should be safe, because compound tag is also a root container of contents in the file...
        final CompoundTag root = (CompoundTag) nbtIs.readTag();
        nbtIs.close();
        ListTag serversListTag = (ListTag) root.getValue().get("servers");
        if(serversListTag.getElementType() != CompoundTag.class) {
            throw new Exception("Type of list value in servers file is not tag_compound!");
        }
        List<CompoundTag> serversList = serversListTag.getValue();
        ServerInfo[] result = new ServerInfo[serversList.size()];
        for(int i = 0; i < serversList.size(); ++i){
            result[i] = createServerFromTag(serversList.get(i).getValue());
        }
        return result;
    }

    public void saveServers(ServerInfo[] servers) throws IOException {

    }
}

