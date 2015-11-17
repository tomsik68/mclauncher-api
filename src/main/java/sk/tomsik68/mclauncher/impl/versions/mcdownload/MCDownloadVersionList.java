package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.api.versions.LatestVersionInformation;
import sk.tomsik68.mclauncher.impl.common.Observable;
import sk.tomsik68.mclauncher.impl.common.Platform;

/**
 * Unified version list for {@link MCDownloadVersion}s. Contains local versions as well as remote.
 */
public final class MCDownloadVersionList extends Observable<String> implements IVersionList, IObserver<String> {
    private final MCDownloadLocalVersionList localVersionList;
    private final MCDownloadOnlineVersionList onlineVersionList;

    /**
     * Creates new MCDownloadVersionList which fetches local JSON files
     * from given minecraft instance
     * @param mc where to fetch JSON files from
     */
    public MCDownloadVersionList(MinecraftInstance mc) {
        onlineVersionList = new MCDownloadOnlineVersionList();
        localVersionList = new MCDownloadLocalVersionList(mc);

        onlineVersionList.addObserver(this);
        localVersionList.addObserver(this);
    }

    @Override
    public void startDownload() throws Exception {
        localVersionList.startDownload();
        onlineVersionList.startDownload();
    }

    @Override
    public IVersion retrieveVersionInfo(String id) throws Exception {
        IVersion result;
        result = localVersionList.retrieveVersionInfo(id);
        if (result == null)
            result = onlineVersionList.retrieveVersionInfo(id);

        if(result != null)
            resolveInheritance((MCDownloadVersion)result);
        return result;
    }

    @Override
    public LatestVersionInformation getLatestVersionInformation() throws Exception {
        return onlineVersionList.getLatestVersionInformation();
    }

    void resolveInheritance(MCDownloadVersion version) throws Exception {
        // version's parent needs to be resolved first
        if (version.getInheritsFrom() != null) {
            MCDownloadVersion parent = (MCDownloadVersion) retrieveVersionInfo(version.getInheritsFrom());
            resolveInheritance(parent);
            version.doInherit(parent);
        }
    }

    @Override
    public void onUpdate(IObservable<String> observable, String changed) {
        notifyObservers(changed);
    }
}
