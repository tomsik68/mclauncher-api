import java.io.File;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

public class TestMCDownloadInstall {

    @Test
    public void test() {
        MCDownloadVersionList list = new MCDownloadVersionList();
        final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));
        list.addObserver(new IObserver<IVersion>() {
            private boolean installed = false;

            @Override
            public void onUpdate(IObservable<IVersion> observable, IVersion changed) {
                if (!changed.getId().equalsIgnoreCase("1.8"))
                    return;
                installed = true;
                System.out.println("Installing " + changed.getDisplayName());
                try {
                    changed.getInstaller().install(changed, mc, new IProgressMonitor() {

                        @Override
                        public void setProgress(int progress) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void setMax(int len) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void incrementProgress(int amount) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void finish() {
                            // TODO Auto-generated method stub

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        try {
            list.startDownload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
