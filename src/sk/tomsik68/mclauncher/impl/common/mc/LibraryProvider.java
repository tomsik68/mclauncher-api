package sk.tomsik68.mclauncher.impl.common.mc;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.services.ILibraryProvider;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.Library;

public class LibraryProvider implements ILibraryProvider {
    private final File libsFolder, binFolder, natives;

    public LibraryProvider(IMinecraftInstance mc) {
        binFolder = mc.getJarProvider().getBinFolder();
        libsFolder = new File(mc.getLocation(), "libraries");
        natives = new File(binFolder, "natives");
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
    public File getLibrary(Library library) {
        String path = library.getPath();
        return new File(libsFolder,path);
    }

    @Override
    public File getNativesDirectory() {
        return natives;
    }

    @Override
    public boolean isInstalled(Library library) {
        return getLibrary(library).exists();
    }

    @Override
    public File getLibrariesDirectory() {
        return libsFolder;
    }

}
