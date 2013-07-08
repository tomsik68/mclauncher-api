package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.io.File;
import java.util.ArrayList;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.net.FileDownload;

public class MCAssetsVersionInstaller implements IVersionInstaller<MCAssetsVersion> {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();

    public MCAssetsVersionInstaller() {

    }

    @Override
    public void install(MCAssetsVersion version, IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        String url = getVersionURL(version.getId());
        mc.getJarProvider().prepareVersionInstallation(version);
        FileDownload.downloadFileWithProgress(url, mc.getJarProvider().getVersionFile(version.getUniqueID()), progress);
        
        File[] lwjgl = mc.getLibraryProvider().getDefaultLWJGLJars();
        boolean update = false;
        for(File file : lwjgl){
            update = update || !file.exists();
        }
        update = update || !mc.getLibraryProvider().getNativesDirectory().exists();
        if(update){
            for(File file : lwjgl){
                url = "http://s3.amazonaws.com/MinecraftDownload/"+file.getName();
                FileDownload.downloadFileWithProgress(url, file, progress);
            }
            mc.getLibraryProvider().getNativesDirectory().mkdirs();
            // TODO natives
            String os = Platform.getCurrentPlatform().getMinecraftName();
            url = "http://s3.amazonaws.com/MinecraftDownload/"+os+"_natives.jar";
            File dest = new File(mc.getLibraryProvider().getNativesDirectory(),"natives_"+os+".jar");
            FileDownload.downloadFileWithProgress(url, dest, progress);
            ExtractUtils.extractZipWithoutRules(dest, mc.getLibraryProvider().getNativesDirectory());
        }
        // TODO check resources
        for (IVersionInstallListener listener : listeners) {
            listener.versionInstalled(version);
        }
    }

    private String getVersionURL(String id) {
        return "http://assets.minecraft.net/" + id + "/minecraft.jar";
    }

    @Override
    public void addVersionInstallListener(IVersionInstallListener listener) {
        listeners.add(listener);
    }

}
