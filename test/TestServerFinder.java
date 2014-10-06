import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Map.Entry;

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
            try {
                while (System.in.available() == 0) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpdate(IObservable<IFoundServer> observable, IFoundServer changed) {
        System.out.println("Found server: " + changed.getName());
        System.out.println(changed.getIP() + ":" + changed.getPort());
        dumpMap(changed.getInformation());
    }

    private void dumpMap(Map<String, Object> information) {
        for (Entry<String, Object> entry : information.entrySet()) {
            System.out.println(String.format("'%s': '%s'", entry.getKey(), entry.getValue().toString()));
        }
    }

}
