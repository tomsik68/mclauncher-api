package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersion;

public interface IVersionInstaller {
    public void install(IVersion changed, IMinecraftInstance mc, IProgressMonitor progress) throws Exception;

    public void addVersionInstallListener(IVersionInstallListener listener);
}
