package sk.tomsik68.mclauncher.impl.common;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

public class Macintosh implements IOperatingSystem {
    private File workDir;

    @Override
    public String getDisplayName() {
        return "MAC OS";
    }

    @Override
    public String getMinecraftName() {
        return "macos";
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
        return workDir;
    }

}
