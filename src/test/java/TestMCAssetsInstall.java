import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersionList;

public class TestMCAssetsInstall {

    @Test
    public void test() {
        try {
            Runtime.getRuntime().exec("mkdir testmc");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));
        final MCAssetsVersionList list = new MCAssetsVersionList();
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
                if (!installed) {
                    installed = true;
                    System.out.println("Found version: " + changed.getDisplayName() + " installing");
                    try {
                        changed.getInstaller().install(changed, mc, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        fail(e.getMessage());

                    }
                }
            }
        });
        try {
            list.startDownload();
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());

        }
    }

}
