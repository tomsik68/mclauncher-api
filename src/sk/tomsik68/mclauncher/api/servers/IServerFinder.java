package sk.tomsik68.mclauncher.api.servers;

import sk.tomsik68.mclauncher.api.common.IObservable;

public interface IServerFinder extends IObservable<IFoundServer>, Runnable {
    public boolean isActive();

    public void startFinding();
}
