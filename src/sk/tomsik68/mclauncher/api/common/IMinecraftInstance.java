package sk.tomsik68.mclauncher.api.common;

import java.io.File;

import sk.tomsik68.mclauncher.api.services.EServiceType;
import sk.tomsik68.mclauncher.api.services.IService;

public interface IMinecraftInstance {
    public File getLocation();

    public IService getService(EServiceType type);

    public IService[] getServices();
}
