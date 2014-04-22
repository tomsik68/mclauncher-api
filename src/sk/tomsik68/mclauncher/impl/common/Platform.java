package sk.tomsik68.mclauncher.impl.common;

import java.util.HashMap;
import java.util.LinkedList;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.Library;

public class Platform {
    public static final LinkedList<IOperatingSystem> oss = new LinkedList<IOperatingSystem>();

    private static IOperatingSystem currentOS = null;
    // macos was renamed to osx in 1.6, so I've created a map of changed OSs
    private static final HashMap<String, String> minecraftOsWrapper = new HashMap<String, String>();
    static {
        oss.add(new LinuxOS());
        oss.add(new WindowsOS());
        oss.add(new MacintoshOS());
        oss.add(new SolarisOS());
        oss.add(new UnknownOS());
        minecraftOsWrapper.put("macos", "osx");
    }

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

    public static void forcePlatform(IOperatingSystem p) {
        currentOS = p;
        Library.addLibraryPathVariable("arch", currentOS.getArchitecture());
    }

    public static String wrapName(String name) {
        if (minecraftOsWrapper.containsKey(name)) {
            name = minecraftOsWrapper.get(name);
        }
        return name;
    }

    public static IOperatingSystem osByName(String name) {
        name = wrapName(name);
        for (IOperatingSystem os : oss) {
            if (os.getMinecraftName().equalsIgnoreCase(name) || name.contains(os.getMinecraftName()) || os.getMinecraftName().contains(name))
                return os;
        }
        return null;
    }
}
