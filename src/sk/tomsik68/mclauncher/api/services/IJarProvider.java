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
     * 
     * @param uniqueID
     *            ID of version
     * @return Path to minecraft.jar for specified version
     */
    public File getVersionFile(String uniqueID);

    /**
     * Prepares this JAR provider for new version installation
     * 
     * @param version
     *            Version which is going to be installed
     */
    public void prepareVersionInstallation(IVersion version);

    /**
     * 
     * @return Path to bin folder
     */
    public File getBinFolder();
}
