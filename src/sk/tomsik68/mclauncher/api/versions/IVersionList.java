package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.IObservable;
/**
 * 
 * @author 
 *
 */
public interface IVersionList extends IObservable<IVersion> {
    public void startDownload() throws Exception;
}
