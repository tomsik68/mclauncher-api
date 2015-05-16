package sk.tomsik68.mclauncher.impl.versions.mcassets;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.FileUtils;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class MCAssetsVersionInstaller implements IVersionInstaller {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();
    private static final String LWJGL_DOWNLOAD_URL = "http://kent.dl.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%202.9.0/lwjgl-2.9.0.zip";
    private static final String RESOURCES_DOWNLOAD_URL = "http://s3.amazonaws.com/MinecraftResources/";

    public MCAssetsVersionInstaller() {

    }

    static File[] getDefaultLWJGLJars(File mcLocation) {
        File oldLib = new File(mcLocation, "oldLib");
        if (!oldLib.exists()) {
            oldLib.mkdirs();
        }
        return new File[]{
                new File(oldLib, "lwjgl.jar"), new File(oldLib, "lwjgl_util.jar"), new File(oldLib, "jinput.jar")
        };
    }

    @Override
    public void install(IVersion version, MinecraftInstance mc, IProgressMonitor progress) throws Exception {
        MCAJarManager jarManager = new MCAJarManager(mc);
        String url = getVersionURL(version.getId());
        if (!jarManager.getVersionFile(version).exists())
            FileUtils.downloadFileWithProgress(url, jarManager.getVersionFile(version), progress);

        File[] lwjgl = getDefaultLWJGLJars(mc.getLocation());
        boolean update = false;
        for (File file : lwjgl) {
            update = update || !file.exists();
        }
        update = update || !jarManager.getNativesDirectory().exists();
        if (update) {
            updateJARs(jarManager, version, progress);
        }
        updateResources(mc.getLocation(), progress);
        notifyListeners(version);

    }

    private void notifyListeners(IVersion version) {
        for (IVersionInstallListener listener : listeners) {
            listener.versionInstalled(version);
        }
    }

    private void updateResources(File mcLocation, IProgressMonitor progress) throws Exception {
        ResourcesXMLParser parser = new ResourcesXMLParser(RESOURCES_DOWNLOAD_URL);
        List<String> resources = parser.parse();
        for (String resource : resources) {
            File dest = getResourceLocation(mcLocation, resource);
            if (!dest.exists()) {
                dest.mkdirs();
                if (!resource.endsWith("/")) {
                    dest.delete();
                    FileUtils.downloadFileWithProgress(RESOURCES_DOWNLOAD_URL + URLEncoder.encode(resource, "UTF-8"), dest,
                            progress);
                }
            }
        }

    }

    private File getResourceLocation(File mcLocation, String resource) {
        File file = new File(mcLocation, "resources" + File.separator + resource.replace('/', File.separatorChar));
        return file;
    }

    private void updateJARs(MCAJarManager jarManager, IVersion version, IProgressMonitor progress) throws Exception {
        File lwjglDir = new File(jarManager.getNativesDirectory(), "lwjgl-2.9.0");
        lwjglDir.deleteOnExit();
        File dest = new File(jarManager.getNativesDirectory(), "lwjgl.zip");
        FileUtils.downloadFileWithProgress(LWJGL_DOWNLOAD_URL, dest, progress);
        jarManager.getNativesDirectory().mkdirs();
        dest.deleteOnExit();
        ExtractUtils.extractZipWithoutRules(dest, jarManager.getNativesDirectory());
        File[] lwjgl = getDefaultLWJGLJars(jarManager.getNativesDirectory());
        // move JARs from LWJGL
        for (File file : lwjgl) {
            FileUtils.copyFile(new File(lwjglDir + File.separator + "jar", file.getName()), file);
        }
        // move natives
        File[] nativeThings = new File(lwjglDir, "native" + File.separator + Platform.getCurrentPlatform().getMinecraftName()).listFiles();
        for (File file : nativeThings) {
            FileUtils.copyFile(file, new File(jarManager.getNativesDirectory(), file.getName()));
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
