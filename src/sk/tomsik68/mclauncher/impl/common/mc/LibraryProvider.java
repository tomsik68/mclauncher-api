package sk.tomsik68.mclauncher.impl.common.mc;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.services.IJarProvider;
import sk.tomsik68.mclauncher.api.services.ILibraryProvider;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.Library;

public class LibraryProvider implements ILibraryProvider {
    private final IJarProvider jarProvider;
    private final File libsFolder, binFolder;

    public LibraryProvider(IMinecraftInstance mc) {
        binFolder = mc.getJarProvider().getBinFolder();
        libsFolder = new File(mc.getLocation(), "libraries");
        this.jarProvider = mc.getJarProvider();
    }

    @Override
    public File[] getDefaultLWJGLJars() {
        return new File[] {
                new File(binFolder, "lwjgl.jar"), new File(binFolder, "lwjgl_util.jar"), new File(binFolder, "jinput.jar")
        };
    }

    @Override
    public File getLibraryFile(Library library) {
        String path = library.getPath().replace('/', File.separatorChar);
        return new File(libsFolder, path);
    }

    @Override
    public File getNativesDirectory(IVersion version) {
        File nativesDir = new File(jarProvider.getVersionFile(version.getUniqueID()).getParentFile(), "natives");
        return nativesDir;
    }

    @Override
    public boolean isInstalled(Library library) {
        return getLibraryFile(library).exists();
    }

    @Override
    public File getLibrariesDirectory() {
        return libsFolder;
    }

}
