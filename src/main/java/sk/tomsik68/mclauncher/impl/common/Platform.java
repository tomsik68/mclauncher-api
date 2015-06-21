package sk.tomsik68.mclauncher.impl.common;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

import java.util.HashMap;
import java.util.LinkedList;

public final class Platform {
    public static final LinkedList<IOperatingSystem> oss = new LinkedList<IOperatingSystem>();
    // macos was renamed to osx in 1.6, so I've created a map of changed OSs
    private static final HashMap<String, String> minecraftOsWrapper = new HashMap<String, String>();

    // static OS lookup map
    static {
        oss.add(new LinuxOS());
        oss.add(new WindowsOS());
        oss.add(new MacintoshOS());
        oss.add(new SolarisOS());
        oss.add(new UnknownOS());
        //                    newName | oldName
        minecraftOsWrapper.put("osx", "macos");
    }

    /** our current operating system */
    private static IOperatingSystem currentOS = null;

    /** tries to determine the current operating system */
    public static IOperatingSystem getCurrentPlatform() {
        if (currentOS != null)
            return currentOS;
        for (IOperatingSystem os : oss) {
            if (os.isCurrent()) {
                forcePlatform(os);
                return currentOS;
            }
        }
        forcePlatform(new UnknownOS());
        return currentOS;
    }

    /** forces current operating system */
    public static void forcePlatform(IOperatingSystem p) {
        MCLauncherAPI.log.fine("Current platform: ".concat(p.getDisplayName()));
        currentOS = p;
    }

    /**
     * Changes "old" name of operating system to "new".
     * Old refers to names of pre-1.6 MC launcher.
     * New refers to names of post-1.6 MC Launcher
     * @param name - Old name of operating system
     * @return New name of operating system
     */
    public static String wrapName(String name) {
        if (minecraftOsWrapper.containsKey(name)) {
            name = minecraftOsWrapper.get(name);
        }
        return name;
    }

    /**
     * Finds {@link IOperatingSystem} by name
     * @param name "New" name of operating system
     * @return IOperatingSystem which suits the criteria
     */
    public static IOperatingSystem osByName(String name) {
        name = wrapName(name);
        for (IOperatingSystem os : oss) {
            if (os.getMinecraftName().equalsIgnoreCase(name) || name.contains(os.getMinecraftName()) || os.getMinecraftName().contains(name))
                return os;
        }
        return null;
    }
}
