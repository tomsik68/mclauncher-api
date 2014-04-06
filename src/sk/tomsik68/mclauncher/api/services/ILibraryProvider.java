package sk.tomsik68.mclauncher.api.services;

import java.io.File;

import sk.tomsik68.mclauncher.impl.versions.mcdownload.Library;

public interface ILibraryProvider {
    public File getLibraryFile(Library lib);

    public File getNativesDirectory();

    public File[] getDefaultLWJGLJars();

    public boolean isInstalled(Library lib);

    public File getLibrariesDirectory();
}
