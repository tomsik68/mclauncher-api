package sk.tomsik68.mclauncher.api.common.mc;

import java.io.File;

import sk.tomsik68.mclauncher.api.services.IJarProvider;
import sk.tomsik68.mclauncher.api.services.ILibraryProvider;

public interface IMinecraftInstance {
    public File getLocation();

    public IJarProvider getJarProvider();

    public ILibraryProvider getLibraryProvider();
}
