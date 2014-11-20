package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.IObservable;

/**
 * Version list contains versions. Versions are not saved, they're passed as
 * they're downloaded using the observer model.
 *
 * @author Tomsik68
 */
public interface IVersionList extends IObservable<IVersion> {
    /**
     * Starts downloading list <b>on this thread</b>
     *
     * @throws Exception Network issues
     */
    public void startDownload() throws Exception;
}
