package sk.tomsik68.mclauncher.impl.common;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

public class Platform {
    private final static IOperatingSystem[] oss = new IOperatingSystem[] { new Linux(), new Solaris(), new Windows(), new Macintosh() };
    private static IOperatingSystem currentOS;

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
        return new UnknownOS();
    }

    public static void forcePlatform(IOperatingSystem p) {
        currentOS = p;
    }
}
