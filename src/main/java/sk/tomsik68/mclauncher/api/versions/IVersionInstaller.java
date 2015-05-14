package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;

/**
 * Installer can install versions of some type
 *
 * @author Tomsik68
 */
public interface IVersionInstaller {
    /**
     * Installs specified version to MC instance
     *
     * @param changed  Version to be installed
     * @param mc       Target MinecraftInstance
     * @param progress ProgressMonitor which will watch the whole progress
     * @throws Exception I/O errors, download errors, incompatibility etc.
     */
    public void install(IVersion changed, MinecraftInstance mc, IProgressMonitor progress) throws Exception;

    /**
     * Adds listener to be notified when version installation finishes. <b>Listeners will be cleared after ONE installation.</b>
     *
     * @param listener Listener to be notified
     */
    public void addVersionInstallListener(IVersionInstallListener listener);
}
