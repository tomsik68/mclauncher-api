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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

final class MCDownloadVersionInstaller implements IVersionInstaller {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();
    private static final String JAR_DOWNLOAD_URL = "https://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.jar";

    public MCDownloadVersionInstaller(){

    }

    @Override
    public void addVersionInstallListener(IVersionInstallListener listener) {
        listeners.add(listener);
    }

    @Override
    public void install(IVersion v, MinecraftInstance mc, IProgressMonitor progress) throws Exception {
        MCDownloadVersionList versionList = new MCDownloadVersionList(mc);
        final boolean haveProgress = (progress != null);

        // create jar manager and library provider as we'll need them
        MCDJarManager jarManager = new MCDJarManager(mc);
        LibraryProvider libraryProvider = new LibraryProvider(mc);
        Logger log = MCLauncherAPI.log;
        log.fine("Checking compatibility...");
        MCDownloadVersion version = (MCDownloadVersion) v;
        // check compatibility of this version
        if (!version.isCompatible())
            throw new VersionIncompatibleException(v);

        // check if inheritance was completed
        if(version.needsInheritance())
            throw new VersionInheritanceException(v);

        // if we're inheriting from a version
        if(version.getInheritsFrom() != null && version.getInheritsFrom().length() > 0){
            MCLauncherAPI.log.fine("Looks like we're inheriting a version. Checking parent version...");
            // download the parent version information
            IVersion parent = versionList.retrieveVersionInfo(version.getInheritsFrom());
            // and the parent version isn't installed
            File jsonFile = jarManager.getInfoFile(parent);
            MCLauncherAPI.log.fine("Looking for ".concat(jsonFile.getAbsolutePath()));
            if (!jsonFile.exists()) {
                MCLauncherAPI.log.info("Installing parent version...");
                parent.getInstaller().install(parent, mc, progress);
            }
        }


        log.fine("Version compatible");
        List<Library> toInstall = version.getLibraries();
        List<Library> toExtract = new ArrayList<Library>();
        log.fine("Fetching libraries...");
        if(haveProgress)
            progress.setStatus("Fetching Libraries...");
        
        log.fine("Platform: " + Platform.getCurrentPlatform().getDisplayName());
        // install all libraries that are needed
        for (Library lib : toInstall) {
            if (lib.isCompatible()) {
                if (!libraryProvider.isInstalled(lib)) {
                    log.finest("Installing " + lib.getName());
                    if(haveProgress)
                        progress.setStatus("Installing " + lib.getName());
                    try {
                        downloadLibrary(lib.getDownloadURL(), libraryProvider.getLibraryFile(lib), progress);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.finest("Failed to install " + lib.getName());
                    }
                }
                // if library has natives, it needs to be extracted...
                if (lib.hasNatives()) {
                    toExtract.add(lib);
                }
            } else {
                log.finest(lib.getName() + " is not compatible.");
            }
        }

        log.fine("Extracting natives...");
        if(haveProgress)
            progress.setStatus("Extracting natives...");
        
        File nativesDir = new File(jarManager.getVersionFolder(version), "natives");
        // purge old natives if they are present
        if (nativesDir.exists()) {
            File[] contains = nativesDir.listFiles();
            for (File f : contains) {
                f.delete();
            }
        }
        log.fine("Extracting libraries...");
        if(haveProgress)
            progress.setStatus("Extracting Libraries...");
        
        // extract the new natives
        for (Library lib : toExtract) {
            File libFile = libraryProvider.getLibraryFile(lib);
            ExtractUtils.extractZipWithRules(libFile, nativesDir, lib.getExtractRules());
        }

        log.fine("Updating resources...");
        if(haveProgress)
            progress.setStatus("Updating Resource...");
        updateResources(mc, version, progress);
        File jarDest = jarManager.getVersionJAR(version);
        File jsonDest = jarManager.getInfoFile(version);
        log.fine("Writing version info JSON file...");
        // always overwrite json file
        FileUtils.writeFile(jsonDest, version.toJSON().toJSONString(JSONStyle.LT_COMPRESS));
        // and jar file
        log.fine("Downloading game JAR...");
        // if this version uses its own jar
        if(version.getJarVersion().equals(version.getId())) {
            // download it
            if(haveProgress)
                progress.setStatus("Downloading Game Jar...");
            try {
                FileUtils.downloadFileWithProgress(JAR_DOWNLOAD_URL.replace("<VERSION>", version.getId()), jarDest, progress);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // notify listeners that installation is finished
        notifyListeners(version);
    }

    private void updateResources(MinecraftInstance mc, MCDownloadVersion version, IProgressMonitor progress) throws Exception {
        MCDResourcesInstaller resInstaller = new MCDResourcesInstaller(mc);
        resInstaller.installAssetsForVersion(version, progress);
    }

    private void downloadLibrary(String url, File dest, IProgressMonitor p) throws Exception {
        FileUtils.downloadFileWithProgress(url, dest, p);
    }

    private void notifyListeners(IVersion version) {
        for (IVersionInstallListener listener : listeners) {
            listener.versionInstalled(version);
        }
    }

}
