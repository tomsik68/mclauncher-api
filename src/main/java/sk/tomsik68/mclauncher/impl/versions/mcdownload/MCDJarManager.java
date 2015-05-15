package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;

import java.io.File;

final class MCDJarManager {
    private final File versionsFolder;

    MCDJarManager(MinecraftInstance mc) {
        versionsFolder = new File(mc.getLocation(), "versions");
    }

    File getVersionFolder(IVersion version){
        return new File(versionsFolder, version.getId());
    }


    File getVersionJAR(IVersion version) {
        return new File(getVersionFolder(version), version.getId() + ".jar");
    }

    File getInfoFile(IVersion version){
        return new File(getVersionFolder(version), version.getId() + ".json");
    }
}
