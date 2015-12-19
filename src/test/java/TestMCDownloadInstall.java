import java.io.File;

import org.junit.Test;

import static org.junit.Assert.*;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

public class TestMCDownloadInstall {

    @Test
    public void test() {

        final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));
        final MCDownloadVersionList list = new MCDownloadVersionList(mc);

        IVersion changed = null;
        try {
            changed = list.retrieveVersionInfo("1.8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Version " + changed.getId());
        System.out.println("Installing " + changed.getDisplayName());
        try {
            changed.getInstaller().install(changed, mc, null);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
