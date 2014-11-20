package sk.tomsik68.mclauncher.api.common;

import java.io.File;

/**
 * Interface for Operating Systems
 *
 * @author Tomsik68
 */
public interface IOperatingSystem {
    /**
     * @return Human-readable name of the operating system e.g. "Windows XP"
     */
    public String getDisplayName();

    /**
     * @return Minecraft's name for this os (win/linux/osx/...)
     */
    public String getMinecraftName();

    /**
     * @return If this pc's operating system matches this interface, return
     * true.
     */
    public boolean isCurrent();

    /**
     * @return Minecraft working directory on this OS
     */
    public File getWorkingDirectory();

    /**
     * @return Architecture of this system
     */
    public String getArchitecture();

}
