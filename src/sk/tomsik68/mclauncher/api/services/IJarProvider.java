package sk.tomsik68.mclauncher.api.services;

import java.io.File;

import sk.tomsik68.mclauncher.api.versions.IVersion;

public interface IJarProvider extends IService {

    public File getVersionFile(String uniqueID);

    public void prepareVersionInstallation(IVersion version);

    public File getBinFolder();
}
