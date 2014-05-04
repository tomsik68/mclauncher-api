import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.servers.IFoundServer;
import sk.tomsik68.mclauncher.impl.servers.VanillaServerFinder;

public class TestServerFinder implements IObserver<IFoundServer> {

    @Test
    public void test() {
        VanillaServerFinder finder;
        try {
            finder = new VanillaServerFinder();
            finder.addObserver(this);
            finder.startFinding();
            while (finder.isActive()) {
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdate(IObservable<IFoundServer> observable, IFoundServer changed) {
        System.out.println("Found server: " + changed.getName());
        System.out.println(changed.getIP()+":"+changed.getPort());
    }

}
