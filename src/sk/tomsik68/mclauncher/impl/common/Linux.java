package sk.tomsik68.mclauncher.impl.common;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

public class Linux implements IOperatingSystem {
    private File workDir;

    @Override
    public String getDisplayName() {
        return "Linux/Unix";
    }

    @Override
    public String getMinecraftName() {
        return "linux";
    }

    @Override
    public boolean isCurrent() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("unix") || os.contains("linux");
    }

    @Override
    public File getWorkingDirectory() {
        if (workDir != null)
            return workDir;
        String userHome = System.getProperty("user.home");
        workDir = new File(userHome, ".minecraft");
        return workDir;
    }
}
