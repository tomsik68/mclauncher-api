package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minidev.json.JSONStyle;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.resources.ResourcesXMLParser;
import sk.tomsik68.mclauncher.util.FileUtils;
import sun.security.action.GetLongAction;

public class MCDownloadVersionInstaller implements IVersionInstaller {
    private static final String JAR_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.jar";
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();
    private final String LIBRARY_BASE_URL = "https://s3.amazonaws.com/Minecraft.Download/libraries/";
    private final String RESOURCES_URL = "http://resources.download.minecraft.net/";

    @Override
    public void addVersionInstallListener(IVersionInstallListener listener) {
        listeners.add(listener);
    }

    @Override
    public void install(IVersion v, IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        Logger log = Logger.getLogger("installer");
        log.info("Checking compatibility...");
        MCDownloadVersion version = (MCDownloadVersion) v;
        if (!version.isCompatible())
            throw new VersionIncompatibleException(v);
        log.info("Version compatible");
        List<Library> toInstall = version.getLibraries();
        log.info("Fetching libraries...");
        for (Library lib : toInstall) {
            if (!mc.getLibraryProvider().isInstalled(lib) && lib.isCompatible()) {
                log.info("Installing " + lib.getName());
                try {
                    installLibrary(lib, mc, progress);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("Failed to install " + lib.getName());
                }
            } else {
                log.info(lib.getName() + " is already installed or incompatible.");
            }
        }
        log.info("Updating resources...");
        updateResources(mc, progress);
        File jarDest = mc.getJarProvider().getVersionFile(version.getUniqueID());
        File jsonDest = new File(jarDest.getParentFile(), "info.json");
        if (!jsonDest.exists())
            FileUtils.writeFile(jsonDest, version.toJSON().toJSONString(JSONStyle.LT_COMPRESS));
        if (!jarDest.exists()) {
            try {
                FileUtils.downloadFileWithProgress(JAR_DOWNLOAD_URL.replace("<VERSION>", version.getId()), jarDest, progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyListeners(version);
    }

    private void updateResources(IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        File assets = new File(mc.getLocation(), "assets");
        if (!assets.exists()) {
            assets.mkdirs();
        }
        ResourcesXMLParser parser = new ResourcesXMLParser(RESOURCES_URL);
        List<String> resources = parser.parse();
        for (String resource : resources) {
            File dest = getResourceLocation(mc.getLocation(), resource);
            if (!dest.exists()) {
                dest.mkdirs();
                if (!resource.endsWith("/")) {
                    dest.delete();
                    try {
                        FileUtils.downloadFileWithProgress(RESOURCES_URL + URLEncoder.encode(resource, "UTF-8"), dest, progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private File getResourceLocation(File location, String resource) {
        File file = new File(location, "assets" + File.separator + resource.replace('/', File.separatorChar));
        return file;
    }

    private void installLibrary(Library lib, IMinecraftInstance mc, IProgressMonitor p) throws Exception {
        String url = LIBRARY_BASE_URL.concat(lib.getPath());
        File dest = new File(mc.getLibraryProvider().getLibrariesDirectory(), lib.getPath());
        dest.mkdirs();
        dest.delete();
        FileUtils.downloadFileWithProgress(url, dest, p);
    }

    private void notifyListeners(IVersion version) {
        for (IVersionInstallListener listener : listeners) {
            listener.versionInstalled(version);
        }
    }

}
