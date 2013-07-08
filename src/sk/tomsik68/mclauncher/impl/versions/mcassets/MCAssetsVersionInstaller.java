package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.io.File;
import java.util.ArrayList;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.FileUtils;

public class MCAssetsVersionInstaller implements IVersionInstaller<MCAssetsVersion> {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();
    private static final String LWJGL_DOWNLOAD_URL = "http://kent.dl.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%202.9.0/lwjgl-2.9.0.zip";
    public MCAssetsVersionInstaller() {

    }

    @Override
    public void install(MCAssetsVersion version, IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        String url = getVersionURL(version.getId());
        mc.getJarProvider().prepareVersionInstallation(version);
        FileUtils.downloadFileWithProgress(url, mc.getJarProvider().getVersionFile(version.getUniqueID()), progress);


        File[] lwjgl = mc.getLibraryProvider().getDefaultLWJGLJars();
        boolean update = false;
        for(File file : lwjgl){
            update = update || !file.exists();
        }
        update = update || !mc.getLibraryProvider().getNativesDirectory().exists();
        if(update){
            File lwjglDir = new File(mc.getLocation(),"lwjgl-2.9.0");
            File dest = new File(mc.getJarProvider().getBinFolder(),"lwjgl.zip");
            FileUtils.downloadFileWithProgress(LWJGL_DOWNLOAD_URL, dest, progress);
            mc.getLibraryProvider().getNativesDirectory().mkdirs();
            dest.deleteOnExit();
            ExtractUtils.extractZipWithoutRules(dest, mc.getLocation());
            
            // move JARs from LWJGL
            for(File file : lwjgl){
                FileUtils.copyFile(new File(lwjglDir+File.separator+"jar",file.getName()),file);
            }
            // move natives
            File[] nativeThings = new File(lwjglDir,"native"+File.separator+Platform.getCurrentPlatform().getMinecraftName()).listFiles();
            for(File file : nativeThings){
                FileUtils.copyFile(file, new File(mc.getLibraryProvider().getNativesDirectory(),file.getName()));
            }
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
