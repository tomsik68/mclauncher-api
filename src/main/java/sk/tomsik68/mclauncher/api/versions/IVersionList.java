package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.IObservable;

/**
 * Version list contains versions. Versions are not saved, their IDs are passed as they're being parsed.
 * If you want IVersion object, you need to retrieve the version
 *
 * @author Tomsik68
 */
public interface IVersionList extends IObservable<String> {
    /**
     * Starts downloading list <b>on this thread</b>
     *
     * @throws Exception Network issues
     */
    public void startDownload() throws Exception;

    /**
     * Retrieves IVersion object with this ID
     * @param id ID of version to retrieve (e.g. "1.8")
     * @return IVersion object for the passed ID
     * @throws Exception - Network errors, JSON parsing errors, version inheritance problems
     */
    public IVersion retrieveVersionInfo(String id) throws Exception;

    /**
     *
     * @return LatestVersionInformation object
     * @throws Exception - Network errors, JSON parsing errors
     */
    public LatestVersionInformation getLatestVersionInformation() throws Exception;
}
