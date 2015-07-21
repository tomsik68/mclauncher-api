package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
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
     * This constructor uses the default minecraft instance for local versions
     */
    public MCDownloadVersionList(){
        this(new MinecraftInstance(Platform.getCurrentPlatform().getWorkingDirectory()));
    }

    public MCDownloadVersionList(MinecraftInstance mc){
        localVersionList = new MCDownloadLocalVersionList(mc);
        onlineVersionList = new MCDownloadOnlineVersionList();
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
        if(result == null)
            result = onlineVersionList.retrieveVersionInfo(id);
        return result;
    }

    @Override
    public LatestVersionInformation getLatestVersionInformation() throws Exception {
        return onlineVersionList.getLatestVersionInformation();
    }

    @Override
    public void onUpdate(IObservable<String> observable, String changed) {
        notifyObservers(changed);
    }
}
