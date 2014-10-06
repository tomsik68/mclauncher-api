package sk.tomsik68.mclauncher.api.services;

import java.io.File;

import sk.tomsik68.mclauncher.api.versions.IVersion;

/**
 * Jar Provider provides JAR files for minecraft
 * 
 * @author Tomsik68
 * 
 */
public interface IJarProvider {

    /**
     * Prepares this JAR provider for new version installation
     * 
     * @param version
     *            Version which is going to be installed
     */
    public void prepareVersionInstallation(IVersion version);

    public File getVersionFile(IVersion version);

}
