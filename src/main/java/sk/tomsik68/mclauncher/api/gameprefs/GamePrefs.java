package sk.tomsik68.mclauncher.api.gameprefs;

import java.io.File;

/**
 * GamePrefs are used for compatibility with the official launcher.
 * Getters/Setters are not documented, only respective fields...
 * @author Tomsik68
 */
public class GamePrefs {
    /** Location of .minecraft folder */
    private File gameDirectory;
    /** Location of JRE */
    private File javaDirectory;
    /** Name of GamePrefs profile */
    private String name;
    /** ID of last version that was played using this GamePrefs */
    private String lastVersion;
    /** arguments for `java` executable */
    private String javaArgs;
    /** UUID of player who owns these GamePrefs */
    private String playerUUID;
    /** Whether or not to use hopper service to report game crashes */
    private boolean useHopper;
    /** Resolution to use for game */
    private Resolution res;

    /** Launcher visibility rules(whether the launcher closes when you start game) */
    private ELauncherVisibility launcherVisibility;

    public File getGameDirectory() {
        return gameDirectory;
    }

    public void setGameDirectory(File gameDirectory) {
        this.gameDirectory = gameDirectory;
    }

    public File getJavaDirectory() {
        return javaDirectory;
    }

    public void setJavaDirectory(File javaDirectory) {
        this.javaDirectory = javaDirectory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public String getJavaArgs() {
        return javaArgs;
    }

    public void setJavaArgs(String javaArgs) {
        this.javaArgs = javaArgs;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public boolean isUseHopper() {
        return useHopper;
    }

    public void setUseHopper(boolean useHopper) {
        this.useHopper = useHopper;
    }

    public Resolution getResolution() {
        return res;
    }

    public void setResolution(Resolution res) {
        this.res = res;
    }

    public ELauncherVisibility getLauncherVisibility() {
        return launcherVisibility;
    }

    public void setLauncherVisibility(ELauncherVisibility launcherVisibility) {
        this.launcherVisibility = launcherVisibility;
    }

    public class Resolution {
        private int width, height;

        public Resolution(int w, int h) {
            width = w;
            height = h;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }

}
