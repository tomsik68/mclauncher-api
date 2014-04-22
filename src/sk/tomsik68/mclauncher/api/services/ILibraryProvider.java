package sk.tomsik68.mclauncher.api.services;

import java.io.File;

import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.Library;

/**
 * LibraryProvider provides JARs of libraries for minecraft
 * 
 * @author Tomsik68
 * 
 */
public interface ILibraryProvider {
    /**
     * 
     * @param lib
     *            Library to find
     * @return JAR of this library
     */
    public File getLibraryFile(Library lib);

    /**
     * @param version
     *            Version to find natives for
     * @return Natives directory for given minecraft version
     */
    public File getNativesDirectory(IVersion version);

    /**
     * 
     * @return Default LWJGL files for older minecraft versions
     */
    public File[] getDefaultLWJGLJars();

    /**
     * 
     * @param lib
     * @return True if specified library is already installed
     */
    public boolean isInstalled(Library lib);

    /**
     * 
     * @return Path to directory where to install libraries
     */
    public File getLibrariesDirectory();
}
