package sk.tomsik68.mclauncher.impl.common.mc;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.services.ILibraryProvider;

public class LibraryProvider implements ILibraryProvider {
    private final File libsFolder, binFolder, natives;

    public LibraryProvider(IMinecraftInstance mc) {
        binFolder = mc.getJarProvider().getBinFolder();
        libsFolder = new File(mc.getLocation(), "libraries");
        natives = new File(binFolder,"natives");
    }

    @Override
    public String getBranding() {
        return "custom";
    }

    @Override
    public File[] getDefaultLWJGLJars() {
        return new File[] { new File(binFolder, "lwjgl.jar"), new File(binFolder, "lwjgl_util.jar"), new File(binFolder, "jinput.jar") };
    }

    @Override
    public File getLibrary(String name) {
        // TODO
        return null;
    }

    @Override
    public File getNativesDirectory() {
        return natives;
    }

}
