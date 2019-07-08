package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONStyle;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.DummyProgressMonitor;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.FileUtils;
import sk.tomsik68.mclauncher.util.IExtractRules;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

final class MCDownloadVersionInstaller implements IVersionInstaller {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();

    public MCDownloadVersionInstaller(){

    }

    @Override
    public void addVersionInstallListener(IVersionInstallListener listener) {
        listeners.add(listener);
    }

    @Override
    public void install(IVersion v, MinecraftInstance mc, IProgressMonitor progress) throws Exception {
        if (progress == null)
            progress = new DummyProgressMonitor();
        MCDownloadVersionList versionList = new MCDownloadVersionList(mc);

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
        List<ArchiveAndRules> nativesToExtract = new ArrayList<>();
        log.fine("Fetching libraries...");
        progress.setStatus("Fetching Libraries...");

        File nativesDir = new File(jarManager.getVersionFolder(version), "natives");

        log.fine("Platform: " + Platform.getCurrentPlatform().getDisplayName());
        // install all libraries that are needed
        for (Library lib : toInstall) {
            if (lib.isCompatible()) {
                log.info("Checking " + lib.getName());
                if (!libraryProvider.isInstalled(lib)) {
                    log.info("Installing " + lib.getName());
                    progress.setStatus("Installing " + lib.getName());
                    try {
                        if (lib.getArtifact() != null)
                            downloadLibrary(lib.getArtifact(), libraryProvider.getLibraryFile(lib), progress);
                        else
                            MCLauncherAPI.log.warning("Skipping library " + lib.getPath() + " as it doesn't supply any artifact");
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.finest("Failed to install " + lib.getName());
                    }
                }
                // if library has natives, it needs to be extracted...
                if (lib.hasNatives()) {
                    Artifact natives = lib.getNatives(Platform.getCurrentPlatform());
                    File dest = new File(libraryProvider.getLibraryFile(lib).getParentFile(), "natives.jar");
                    downloadLibrary(natives, dest, progress);
                    nativesToExtract.add(new ArchiveAndRules(dest, lib.getExtractRules()));
                }
            } else {
                log.finest(lib.getName() + " is not compatible.");
            }
        }

        log.fine("Extracting natives...");
        progress.setStatus("Extracting natives...");
        
        // purge old natives if they are present
        if (nativesDir.exists()) {
            File[] contains = nativesDir.listFiles();
            for (File f : contains) {
                f.delete();
            }
        }
        log.fine("Extracting libraries...");
        progress.setStatus("Extracting Libraries...");
        
        // extract the new natives
        for (ArchiveAndRules ar : nativesToExtract) {
            ExtractUtils.extractZipWithRules(ar.getArchive(), nativesDir, ar.getRules());
        }

        log.fine("Updating resources...");
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
            progress.setStatus("Downloading Game Jar...");
            try {
                FileUtils.downloadFileWithProgress(version.getClient().getUrl(), jarDest, progress);
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

    private void downloadLibrary(Artifact artifact, File dest, IProgressMonitor p) throws Exception {
        MCLauncherAPI.log.info("Downloading library " + dest.getPath());
        FileUtils.downloadFileWithProgress(artifact.getUrl(), dest, p);
    }

    private void notifyListeners(IVersion version) {
        for (IVersionInstallListener listener : listeners) {
            listener.versionInstalled(version);
        }
    }

    private static final class ArchiveAndRules {
        private final File archive;
        private final IExtractRules rules;

        ArchiveAndRules(File archive, IExtractRules rules) {
            this.archive = archive;
            this.rules = rules;
        }

        public File getArchive() {
            return archive;
        }

        public IExtractRules getRules() {
            return rules;
        }
    }

}
