package sk.tomsik68.mclauncher.impl.servers;

import sk.tomsik68.mclauncher.api.servers.IFoundServer;
import sk.tomsik68.mclauncher.api.servers.IServerFinder;
import sk.tomsik68.mclauncher.impl.common.Observable;

public class VanillaServerFinder extends Observable<IFoundServer> implements IServerFinder {
    private Thread thread;

    @Override
    public void run() {
        while (isActive()) {

        }
    }

    @Override
    public boolean isActive() {
        return thread.isAlive();
    }

    @Override
    public void startFinding() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void stop() {
        thread.interrupt();
    }

}
