package sk.tomsik68.mclauncher.api.services;

import java.io.File;

public interface ILibraryProvider extends IService {
    public File[] getDefaultLWJGLJars();

    /**
     * 
     * @param name
     *            - Name of library.
     * @return - File where this version of this library is located
     */
    public File getLibrary(String name);

    public File getNativesDirectory();
}
