package sk.tomsik68.mclauncher.api.services;

import java.io.File;

import sk.tomsik68.mclauncher.impl.versions.mcdownload.Library;

public interface ILibraryProvider extends IService {
    /**
     * 
     * @param name
     *            - Name of library.
     * @return - File where this library is located. Name will be in minecraft's format.
     */
    public File getLibrary(Library lib);

    public File getNativesDirectory();

    public File[] getDefaultLWJGLJars();

    public boolean isInstalled(Library lib);

    public File getLibrariesDirectory();
}
