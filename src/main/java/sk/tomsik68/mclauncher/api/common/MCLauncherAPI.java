package sk.tomsik68.mclauncher.api.common;

import sk.tomsik68.mclauncher.impl.common.Platform;

import java.util.logging.Logger;

/**
 * This is main control class of MCLauncherAPI. It stores some constants and
 * information needed by other components of the API.
 *
 * @author Tomsik68
 */
public class MCLauncherAPI {
    public static final int MC_LAUNCHER_VERSION = 15;
    public static Logger log = Logger.getLogger(MCLauncherAPI.class.getName());

    static {
        Platform.getCurrentPlatform();
    }
}
