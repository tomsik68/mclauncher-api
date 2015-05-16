import org.junit.Test;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.servers.ServerInfo;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.common.mc.VanillaServerStorage;

import java.io.File;

public class TestVanillaServerStorage {

    @Test
    public void test(){
        MinecraftInstance mc = new MinecraftInstance(Platform.getCurrentPlatform().getWorkingDirectory());
        VanillaServerStorage serverStorage = new VanillaServerStorage(mc);
        try {

            ServerInfo[] servers = serverStorage.loadServers();
            for (ServerInfo server : servers) {
                System.out.println(server.getName() + " - " + server.getIP() + ":" + server.getPort());
            }
            VanillaServerStorage testStorage = new VanillaServerStorage(new File("servers_savetest.dat"));
            testStorage.saveServers(servers);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
