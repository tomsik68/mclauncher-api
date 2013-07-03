package sk.tomsik68.mclauncher.api.common;

import java.io.File;

public interface IOperatingSystem {
    public String getDisplayName();

    public String getMinecraftName();

    public boolean isCurrent();

    public File getWorkingDirectory();
}
