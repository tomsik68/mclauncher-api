package sk.tomsik68.mclauncher.impl.versions.common;

import java.util.Arrays;
import java.util.List;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.impl.common.Observable;

public class MultipleVersionLists extends Observable<IVersion> implements IVersionList, IObserver<IVersion> {
    private final List<IVersionList> versionLists;
    public MultipleVersionLists(IVersionList... lists){
        versionLists = Arrays.asList(lists);
        for(IVersionList list : lists){
            list.addObserver(this);
        }
    }
    @Override
    public void startDownload() throws Exception {
        for(IVersionList list : versionLists){
            list.startDownload();
        }
    }

    @Override
    public void onUpdate(IObservable<IVersion> observable, IVersion changed) {
        notifyObservers(changed);
    }

}
