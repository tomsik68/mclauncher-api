package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.resources.ResourcesXMLParser;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.FileUtils;

@Deprecated
public class MCAssetsVersionInstaller implements IVersionInstaller {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();

    public MCAssetsVersionInstaller() {

    }

    @Override
    public void install(IVersion version, IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        String url = getVersionURL(version.getId());
        mc.getJarProvider().prepareVersionInstallation(version);
        if (!mc.getJarProvider().getVersionFile(version.getUniqueID()).exists())
            FileUtils.downloadFileWithProgress(url, mc.getJarProvider().getVersionFile(version.getUniqueID()), progress);

        File[] lwjgl = mc.getLibraryProvider().getDefaultLWJGLJars();
        boolean update = false;
        for (File file : lwjgl) {
            update = update || !file.exists();
        }
        update = update || !mc.getLibraryProvider().getNativesDirectory().exists();
        if (update) {
            updateJARs(mc, progress);
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
        ResourcesXMLParser parser = new ResourcesXMLParser(MCLauncherAPI.URLS.RESOURCES_DOWNLOAD_URL);
        List<String> resources = parser.parse();
        for (String resource : resources) {
            File dest = getResourceLocation(mcLocation, resource);
            if (!dest.exists()) {
                dest.mkdirs();
                if (!resource.endsWith("/")) {
                    dest.delete();
                    FileUtils.downloadFileWithProgress(MCLauncherAPI.URLS.RESOURCES_DOWNLOAD_URL + URLEncoder.encode(resource, "UTF-8"), dest, progress);
                }
            }
        }

    }

    private File getResourceLocation(File mcLocation, String resource) {
        File file = new File(mcLocation, "resources" + File.separator + resource.replace('/', File.separatorChar));
        return file;
    }

    private void updateJARs(IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        File lwjglDir = new File(mc.getLocation(), "lwjgl-2.9.0");
        lwjglDir.deleteOnExit();
        File dest = new File(mc.getJarProvider().getBinFolder(), "lwjgl.zip");
        FileUtils.downloadFileWithProgress(MCLauncherAPI.URLS.LWJGL_DOWNLOAD_URL, dest, progress);
        mc.getLibraryProvider().getNativesDirectory().mkdirs();
        dest.deleteOnExit();
        ExtractUtils.extractZipWithoutRules(dest, mc.getLocation());
        File[] lwjgl = mc.getLibraryProvider().getDefaultLWJGLJars();
        // move JARs from LWJGL
        for (File file : lwjgl) {
            FileUtils.copyFile(new File(lwjglDir + File.separator + "jar", file.getName()), file);
        }
        // move natives
        File[] nativeThings = new File(lwjglDir, "native" + File.separator + Platform.getCurrentPlatform().getMinecraftName()).listFiles();
        for (File file : nativeThings) {
            FileUtils.copyFile(file, new File(mc.getLibraryProvider().getNativesDirectory(), file.getName()));
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
