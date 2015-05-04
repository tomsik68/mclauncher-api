package sk.tomsik68.mclauncher.api.servers;

import sk.tomsik68.mclauncher.api.common.IObservable;

/**
 * Server finder, most likely a thread, which looks for servers on local
 * network. To receive events, use observer
 *
 * @author Tomsik68
 */
public interface IServerFinder extends IObservable<FoundServerInfo>, Runnable {
    /**
     * @return True if this finder is active
     */
    public boolean isActive();

    /**
     * Starts this finder
     */
    public void startFinding();

    /**
     * Stops this finder
     */
    public void stop();
}
