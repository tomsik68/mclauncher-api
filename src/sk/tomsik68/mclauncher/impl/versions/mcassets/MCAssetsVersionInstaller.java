package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.util.ArrayList;

import sk.tomsik68.mclauncher.api.common.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.net.FileDownload;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;

public class MCAssetsVersionInstaller implements IVersionInstaller<MCAssetsVersion> {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();

    public MCAssetsVersionInstaller() {

    }

    @Override
    public void install(MCAssetsVersion version, IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        String url = getVersionURL(version.getId());
        mc.getJarProvider().prepareVersionInstallation(version);
        FileDownload.downloadFileWithProgress(url, mc.getJarProvider().getVersionFile(version.getUniqueID()), progress);
        for (IVersionInstallListener listener : listeners) {
            listener.versionInstalled(version);
        }
        // TODO install LWJGL if not installed
    }

    private String getVersionURL(String id) {
        return "http://assets.minecraft.net/" + id + "/minecraft.jar";
    }

    @Override
    public void addVersionInstallListener(IVersionInstallListener listener) {
        listeners.add(listener);
    }

}
