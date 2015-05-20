package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONStyle;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

final class MCDownloadVersionInstaller implements IVersionInstaller {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();
    private static final String JAR_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.jar";

    @Override
    public void addVersionInstallListener(IVersionInstallListener listener) {
        listeners.add(listener);
    }

    @Override
    public void install(IVersion v, MinecraftInstance mc, IProgressMonitor progress) throws Exception {
        // create jar manager and library provider as we'll need them
        MCDJarManager jarManager = new MCDJarManager(mc);
        LibraryProvider libraryProvider = new LibraryProvider(mc);
        Logger log = MCLauncherAPI.log;
        log.info("Checking compatibility...");
        MCDownloadVersion version = (MCDownloadVersion) v;
        // check compatibility of this version
        if (!version.isCompatible())
            throw new VersionIncompatibleException(v);
        // check if inheritance was completed
        if(version.needsInheritance())
            throw new VersionInheritanceException(v);

        log.info("Version compatible");
        List<Library> toInstall = version.getLibraries();
        List<Library> toExtract = new ArrayList<Library>();
        log.info("Fetching libraries...");
        log.info("Platform: " + Platform.getCurrentPlatform().getDisplayName());
        // install all libraries that are needed
        for (Library lib : toInstall) {
            if (lib.isCompatible()) {
                if (!libraryProvider.isInstalled(lib)) {
                    log.info("Installing " + lib.getName());
                    try {
                        downloadLibrary(lib.getDownloadURL(), libraryProvider.getLibraryFile(lib), progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.info("Failed to install " + lib.getName());
                    }
                }
                // if library has natives, it needs to be extracted...
                if (lib.hasNatives()) {
                    toExtract.add(lib);
                }
            } else {
                log.info(lib.getName() + " is not compatible.");
            }
        }

        log.info("Extracting natives...");
        File nativesDir = new File(jarManager.getVersionFolder(version), "natives");
        // purge old natives if they are present
        if (nativesDir.exists()) {
            File[] contains = nativesDir.listFiles();
            for (File f : contains) {
                f.delete();
            }
        }
        log.info("Extracting libraries...");
        // extract the new natives
        for (Library lib : toExtract) {
            File libFile = libraryProvider.getLibraryFile(lib);
            ExtractUtils.extractZipWithRules(libFile, nativesDir, lib.getExtractRules());
        }

        log.info("Updating resources...");
        updateResources(mc, version, progress);
        File jarDest = jarManager.getVersionJAR(version);
        File jsonDest = jarManager.getInfoFile(version);
        // always overwrite json file
        FileUtils.writeFile(jsonDest, version.toJSON().toJSONString(JSONStyle.LT_COMPRESS));
        // and jar file
        try {
            FileUtils.downloadFileWithProgress(JAR_DOWNLOAD_URL.replace("<VERSION>", version.getId()), jarDest, progress);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // notify listeners that installation is finished
        notifyListeners(version);
        if (progress != null)
            progress.finish();
    }

    private void updateResources(MinecraftInstance mc, MCDownloadVersion version, IProgressMonitor progress) throws Exception {
        MCDResourcesInstaller resInstaller = new MCDResourcesInstaller(mc);
        resInstaller.installAssetsForVersion(version, progress);
    }

    private void downloadLibrary(String url, File dest, IProgressMonitor p) throws Exception {
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
