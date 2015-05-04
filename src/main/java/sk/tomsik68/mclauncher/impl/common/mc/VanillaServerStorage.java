package sk.tomsik68.mclauncher.impl.common.mc;

import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.TagType;
import com.flowpowered.nbt.stream.NBTInputStream;
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

    public VanillaServerStorage(File file){
        this.file = file;
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
            //result[i] = serversList.get(i);
        }
        return result;
    }

    public void saveServers(ServerInfo[] servers) throws IOException {

    }
}

