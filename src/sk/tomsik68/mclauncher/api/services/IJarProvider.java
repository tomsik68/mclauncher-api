package sk.tomsik68.mclauncher.api.services;

import java.io.File;

import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersion;

public interface IJarProvider extends IService {

    public File getVersionLocation(String uniqueID);

    public void prepareVersionInstallation(IVersion version);
    
}
