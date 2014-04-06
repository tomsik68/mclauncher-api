package sk.tomsik68.mclauncher.api.services;

import java.io.File;

import sk.tomsik68.mclauncher.api.versions.IVersion;
/**
 * Jar Provider
 * @author Tomsik68
 *
 */
public interface IJarProvider {

    public File getVersionFile(String uniqueID);

    public void prepareVersionInstallation(IVersion version);

    public File getBinFolder();
}
