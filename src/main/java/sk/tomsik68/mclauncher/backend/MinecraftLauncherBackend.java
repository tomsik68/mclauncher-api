package sk.tomsik68.mclauncher.backend;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.mods.IModdingProfile;
import sk.tomsik68.mclauncher.api.servers.ServerInfo;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.LatestVersionInformation;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

import java.io.File;
import java.util.List;

/**
 * This is multi-purpose class that simplifies access to many mclauncher-api components
 */
public final class MinecraftLauncherBackend {
    private final MinecraftInstance minecraftInstance;
    private static final ILaunchSettings DEFAULT_LAUNCH_SETTINGS = new DefaultLaunchSettings();

    public MinecraftLauncherBackend(File minecraftDirectory){
        minecraftInstance = new MinecraftInstance(minecraftDirectory);
    }

    private IVersion findVersion(String versionID) throws Exception {
        MCDownloadVersionList mcDownloadVersionList = new MCDownloadVersionList();
        return mcDownloadVersionList.retrieveVersionInfo(versionID);
    }

    public LatestVersionInformation getLatestVersionInformation() throws Exception {
        MCDownloadVersionList mcDownloadVersionList = new MCDownloadVersionList();
        return mcDownloadVersionList.getLatestVersionInformation();
    }

    /**
     *
     * @return List of versions that can be installed
     * @throws Exception Network I/O, JSON Parsing errors
     */
    public List<String> getVersionList() throws Exception {
        AddToListObserver observer = new AddToListObserver();
        MCDownloadVersionList mcDownloadVersionList = new MCDownloadVersionList();
        mcDownloadVersionList.addObserver(observer);
        mcDownloadVersionList.startDownload();

        List<String> result = observer.getList();
        return result;
    }

    /**
     * Checks if local minecraft copy needs updating. Everything is checked - libraries, assets, game jar
     * This should be run everytime before you launch.
     * @param versionID - ID of version to check for update.
     * @param progress - {@link IProgressMonitor} in case you want to track the update progress. It may be null
     * @throws Exception - Network errors, JSON parsing
     */
    public void updateMinecraft(String versionID, IProgressMonitor progress) throws Exception {
        if (versionID == null || versionID.length() == 0) throw new NullPointerException("versionID can't be null!");
        IVersion version = findVersion(versionID);
        version.getInstaller().install(version, minecraftInstance, progress);
    }

    /**
     * Returns a {@link ProcessBuilder} which has minecraft command inside of it. Use <code>ProcessBuilder.start()</code> to start the process.
     * @param session - Authentication session
     * @param versionID - Version ID to run
     * @return ProcessBuilder which has minecraft command inside of it. No other things are setup.
     * @throws Exception - Network errors, JSON parsing, process failures
     */
    public ProcessBuilder launchMinecraft(ISession session, String versionID) throws Exception {
        return launchMinecraft(session, null, versionID, DEFAULT_LAUNCH_SETTINGS, null);
    }

    /**
     * Returns a {@link ProcessBuilder} which has minecraft command inside of it. Use <code>ProcessBuilder.start()</code> to start the process.
     * @param session - Authentication session
     * @param serverInfo - Server to connect to. May be null.
     * @param versionID - Version ID to run
     * @param launchSettings - Launch settings(amount of RAM etc)
     * @param moddingProfile - Modding profile information(overriding game jar etc.). May be null.
     * @return ProcessBuilder which has minecraft command inside of it. No other things are setup.
     * @throws Exception - Network errors, JSON parsing, process failures
     */
    public ProcessBuilder launchMinecraft(ISession session, ServerInfo serverInfo, String versionID, ILaunchSettings launchSettings, IModdingProfile moddingProfile) throws Exception {
        if(versionID == null || versionID.length() == 0) throw new NullPointerException("versionID can't be null!");

        IVersion version = findVersion(versionID);

        if(session == null) throw new NullPointerException("session can't be null!");
        if(launchSettings == null) throw new NullPointerException("launchSettings can't be null!");
        List<String> launchCommand = version.getLauncher().getLaunchCommand(session, minecraftInstance, serverInfo, version, launchSettings, moddingProfile);
        ProcessBuilder pb = new ProcessBuilder();
        pb.command(launchCommand);
        return pb;
    }
}
