package sk.tomsik68.mclauncher.api.gameprefs;

import java.io.File;

/**
 * GamePrefs are used for compatibility with the official launcher.
 *
 * @author Tomsik68
 */
public class GamePrefs {
    private File gameDirectory, javaDirectory;
    private String name, lastVersion, javaArgs, playerUUID;
    private boolean useHopper;
    private Resolution res;
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
