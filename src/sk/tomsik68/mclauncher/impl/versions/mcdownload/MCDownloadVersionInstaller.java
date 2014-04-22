package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.minidev.json.JSONStyle;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.assets.MCDResourcesInstaller;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.FileUtils;

public class MCDownloadVersionInstaller implements IVersionInstaller {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();

    @Override
    public void addVersionInstallListener(IVersionInstallListener listener) {
        listeners.add(listener);
    }

    @Override
    public void install(IVersion v, IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        Logger log = MCLauncherAPI.log;
        log.info("Checking compatibility...");
        MCDownloadVersion version = (MCDownloadVersion) v;
        if (!version.isCompatible())
            throw new VersionIncompatibleException(v);
        log.info("Version compatible");
        List<Library> toInstall = version.getLibraries();
        List<Library> toExtract = new ArrayList<Library>();
        log.info("Fetching libraries...");

        for (Library lib : toInstall) {
            if (lib.isCompatible()) {
                if (!mc.getLibraryProvider().isInstalled(lib)) {
                    log.info("Installing " + lib.getName());
                    try {
                        downloadLibrary(lib, mc, progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("Failed to install " + lib.getName());
                    }
                }
                if (lib.hasNatives()) {
                    toExtract.add(lib);
                }
            } else {
                log.info(lib.getName() + " is not compatible.");
            }
        }

        log.info("Extracting natives...");
        File nativesDir = mc.getLibraryProvider().getNativesDirectory(version);
        // purge old natives
        if (nativesDir.exists()) {
            File[] contains = nativesDir.listFiles();
            for (File f : contains) {
                f.delete();
            }
        }
        for (Library lib : toExtract) {
            File libFile = mc.getLibraryProvider().getLibraryFile(lib);
            ExtractUtils.extractZipWithRules(libFile, nativesDir, lib.getExtractRules());
        }

        log.info("Updating resources...");
        updateResources(mc, version, progress);
        File jarDest = mc.getJarProvider().getVersionFile(version.getUniqueID());
        File jsonDest = new File(jarDest.getParentFile(), "info.json");
        if (!jsonDest.exists())
            FileUtils.writeFile(jsonDest, version.toJSON().toJSONString(JSONStyle.LT_COMPRESS));
        if (!jarDest.exists()) {
            try {
                MCLauncherAPI.log.info("Downloading JSON version descriptor");
                FileUtils.downloadFileWithProgress(MCLauncherAPI.URLS.NEW_JAR_DOWNLOAD_URL.replace("<VERSION>", version.getId()), jarDest, progress);
                MCLauncherAPI.log.info("Descriptor downloaded");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        notifyListeners(version);
        if (progress != null)
            progress.finish();
    }

    private void updateResources(IMinecraftInstance mc, MCDownloadVersion version, IProgressMonitor progress) throws Exception {
        File assets = mc.getAssetsDirectory();
        if (!assets.exists()) {
            assets.mkdirs();
        }
        MCDResourcesInstaller resInstaller = new MCDResourcesInstaller(mc);
        resInstaller.install(version, progress);
    }

    private void downloadLibrary(Library lib, IMinecraftInstance mc, IProgressMonitor p) throws Exception {
        String url = MCLauncherAPI.URLS.LIBRARY_BASE_URL.concat(lib.getPath());
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
