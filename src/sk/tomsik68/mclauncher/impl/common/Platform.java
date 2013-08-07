package sk.tomsik68.mclauncher.impl.common;

import java.util.HashMap;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

public class Platform {
    private final static IOperatingSystem[] oss = new IOperatingSystem[] { new Linux(), new Solaris(), new Windows(), new Macintosh() };
    private static IOperatingSystem currentOS = null;
    // macos was renamed to osx in 1.6, so I've created a map of changed OSs
    private static final HashMap<String, String> minecraftOsWrapper = new HashMap<String, String>();
    static {
        minecraftOsWrapper.put("osx", "macos");
    }

    public static IOperatingSystem getCurrentPlatform() {
        if (currentOS != null)
            return currentOS;
        for (IOperatingSystem os : oss) {
            if (os.isCurrent()) {
                currentOS = os;
                return currentOS;
            }
        }
        currentOS = new UnknownOS();
        return currentOS;
    }

    public static void forcePlatform(IOperatingSystem p) {
        currentOS = p;
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
