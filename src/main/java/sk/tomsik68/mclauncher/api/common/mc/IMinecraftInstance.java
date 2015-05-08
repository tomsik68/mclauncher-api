package sk.tomsik68.mclauncher.api.common.mc;

import sk.tomsik68.mclauncher.api.servers.IServerStorage;
import sk.tomsik68.mclauncher.api.services.IJarProvider;
import sk.tomsik68.mclauncher.api.services.ILibraryProvider;

import java.io.File;

/**
 * Interface for Minecraft Instances. Minecraft Instance allows for different
 * file organization.
 *
 * @author Tomsik68
 */
public interface IMinecraftInstance {
    /**
     * @return Working directory
     */
    public File getLocation();

    /**
     * @return JarProvider for this Minecraft Instance
     */
    public IJarProvider getJarProvider();

    /**
     * @return LibraryProvider for this Minecraft Instance
     */
    public ILibraryProvider getLibraryProvider();

    /**
     * @return Location of .minecraft/assets
     */
    public File getAssetsDirectory();

    public IServerStorage getServerStorage();

}
