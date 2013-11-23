package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;
import java.io.FileInputStream;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

public class MCDownloadVersionLauncher implements IVersionLauncher {

    @Override
    public Process launch(ISession session, IMinecraftInstance mc, ISavedServer server, IVersion v, ILaunchSettings settings) throws Exception {
        File jsonFile = new File(mc.getJarProvider().getVersionFile(v.getUniqueID()).getParent(), "info.json");
        if (!jsonFile.exists()) {
            throw new RuntimeException("You need to download the version at first!");
        }
        MCDownloadVersion version = new MCDownloadVersion((JSONObject) JSONValue.parse(new FileInputStream(jsonFile)));
        mc.getJarProvider().getVersionFile(version.getUniqueID());

        return null;
    }

}
