package sk.tomsik68.mclauncher.impl.common.mc;

import com.flowpowered.nbt.*;
import com.flowpowered.nbt.stream.NBTInputStream;
import com.flowpowered.nbt.stream.NBTOutputStream;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.servers.ServerInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Server storage implementation used by the default launcher
 */
public final class VanillaServerStorage {
    private final File file;
    private static final int DEFAULT_PORT = 25565;

    public VanillaServerStorage(MinecraftInstance mc){
        this(new File(mc.getLocation(), "servers.dat"));
    }

    public VanillaServerStorage(File file){
        this.file = file;
    }


    private static ServerInfo createServerFromTag(CompoundMap compound){
        String ipString, ip, name, icon = null;
        int port;
        ipString = compound.get("ip").getValue().toString();
        name = compound.get("name").getValue().toString();

        if(ipString.contains(":")){
            ip = ipString.split(":")[0];
            port = Integer.parseInt(ipString.split(":")[1]);
        } else {
            ip = ipString;
            port = DEFAULT_PORT;
        }

        if(compound.containsKey("icon")){
            icon = ((StringTag)compound.get("icon")).getValue();
        }


        return new ServerInfo(ip, name, icon,port);
    }


    public ServerInfo[] loadServers() throws Exception{
        final FileInputStream fis = new FileInputStream(file);
        NBTInputStream nbtIs = new NBTInputStream(fis, false);
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

    private CompoundMap createCompoundFromServer(ServerInfo server){
        CompoundMap result = new CompoundMap();
        result.put("name", new StringTag("name", server.getName()));
        String ipString = server.getIP();
        if(server.getPort() != DEFAULT_PORT)
            ipString = ipString.concat(":".concat(Integer.toString(server.getPort())));

        result.put("ip", new StringTag("ip", ipString));
        if(server.hasIcon())
            result.put("icon", new StringTag("icon", server.getIcon()));
        return result;
    }


    public void saveServers(ServerInfo[] servers) throws IOException {
        if(file.exists())
            if(!file.delete())
                throw new IOException("Could not overwrite '".concat(file.getAbsolutePath()).concat("'"));
        final FileOutputStream fos = new FileOutputStream(file);
        NBTOutputStream nbtOutputStream = new NBTOutputStream(fos, false);
        ArrayList<CompoundTag> serversList = new ArrayList<CompoundTag>();

        for(ServerInfo server : servers){
            serversList.add(new CompoundTag("", createCompoundFromServer(server)));
        }

        ListTag<CompoundTag> listTag = new ListTag<CompoundTag>("servers", CompoundTag.class, serversList);
        CompoundTag root = new CompoundTag("", new CompoundMap());
        root.getValue().put("servers", listTag);
        nbtOutputStream.writeTag(root);
        nbtOutputStream.flush();
        fos.flush();
        nbtOutputStream.close();
        fos.close();
    }
}

