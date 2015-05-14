package sk.tomsik68.mclauncher.impl.common.mc;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.services.IJarProvider;
import sk.tomsik68.mclauncher.api.services.ILibraryProvider;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.Library;

import java.io.File;

final class VanillaLibraryProvider implements ILibraryProvider {
    private final IJarProvider jarProvider;
    private final File libsFolder, binFolder;

    public VanillaLibraryProvider(IMinecraftInstance mc) {
        binFolder = new File(mc.getLocation(), "oldLib");
        libsFolder = new File(mc.getLocation(), "libraries");
        this.jarProvider = mc.getJarProvider();
    }

    @Override
    public File getLibraryFile(Library library) {
        String path = library.getPath().replace('/', File.separatorChar);
        return new File(libsFolder, path);
    }

    @Override
    public File getNativesDirectory(IVersion version) {
        File nativesDir = new File(jarProvider.getVersionFile(version).getParentFile(), "natives");
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
