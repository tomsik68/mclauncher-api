package sk.tomsik68.mclauncher.impl.common;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

import java.io.File;

final class MacintoshOS implements IOperatingSystem {
    private File workDir;

    @Override
    public String getDisplayName() {
        return "MAC OS";
    }

    @Override
    public String getMinecraftName() {
        return "osx";
    }

    @Override
    public boolean isCurrent() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    @Override
    public File getWorkingDirectory() {
        if (workDir != null)
            return workDir;
        workDir = new File(System.getProperty("user.home"), "Library/Application Support/.minecraft");
        MCLauncherAPI.log.fine("Minecraft working directory: ".concat(workDir.getAbsolutePath()));
        return workDir;
    }

    @Override
    public String getArchitecture() {
        return System.getProperty("sun.arch.data.model");
    }

}
