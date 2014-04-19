package sk.tomsik68.mclauncher.api.common;

import java.util.logging.Logger;
/**
 * This is main entrypoint of MCLauncherAPI. It stores some constants and information needed by other components of the API.
 * @author Tomsik68
 *
 */
public class MCLauncherAPI {
    public static final int MC_LAUNCHER_VERSION = 14;
    public static Logger log = Logger.getLogger(MCLauncherAPI.class.getName());

    public static final class URLS {
        public static final String RESOURCES_URL = "http://resources.download.minecraft.net/";
        public static final String RESOURCES_INDEX_URL = "https://s3.amazonaws.com/Minecraft.Download/indexes/";
        public final static String LIBRARY_BASE_URL = "https://libraries.minecraft.net/";
        public static final String NEW_JAR_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.jar";
        public static final String JSONVERSION_LIST_URL = "http://s3.amazonaws.com/Minecraft.Download/versions/versions.json";
        public static final String NEW_VERSION_URL = "http://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.json";
        
        public static final String PASSWORD_LOGIN_URL = "https://authserver.mojang.com/authenticate";
        public static final String SESSION_LOGIN_URL = "https://authserver.mojang.com/refresh";
        public static final String SESSION_LOGOUT_URL = "https://authserver.mojang.com/invalidate";

        public static final String LWJGL_DOWNLOAD_URL = "http://kent.dl.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%202.9.0/lwjgl-2.9.0.zip";
        public static final String RESOURCES_DOWNLOAD_URL = "http://s3.amazonaws.com/MinecraftResources/";
    }
}
