import java.io.File;

import org.junit.Test;
import static org.junit.Assert.*;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

public class TestMCDownloadInstall {

    @Test
    public void test() {
        MCLauncherAPI.log.info("Test for a test");
        final MCDownloadVersionList list = new MCDownloadVersionList();
        final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));
        list.addObserver(new IObserver<String>() {
            private boolean installed = false;

            @Override
            public void onUpdate(IObservable<String> observable, String id) {
                IVersion changed = null;
                try {
                    changed = list.retrieveVersionInfo(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Version "+changed.getId());
                if(installed) return;
                installed = true;
                System.out.println("Installing " + changed.getDisplayName());
                try {
                    changed.getInstaller().install(changed, mc, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail();
                }
            }
        });
        try {
            list.startDownload();
        } catch (Exception e) {
            e.printStackTrace();
            fail();

        }
    }
}
