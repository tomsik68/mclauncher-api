package sk.tomsik68.mclauncher.api.common;

import sk.tomsik68.mclauncher.impl.common.Platform;

import java.util.logging.Logger;

/**
 * This is main control class of MCLauncherAPI.
 * It stores global constants like Minecraft launcher version it's compatible with and global logger.
 *
 * @author Tomsik68
 */
public class MCLauncherAPI {
    public static final int MC_LAUNCHER_VERSION = 18;
    public static Logger log = Logger.getLogger(MCLauncherAPI.class.getName());

    static {
        Platform.getCurrentPlatform();
    }
}
