import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

public class TestMCDownloadList {

    @Test
    public void test() {
        MCDownloadVersionList list = new MCDownloadVersionList();
        list.addObserver(new IObserver<String>() {
            @Override
            public void onUpdate(IObservable<String> observable, String changed) {
                System.out.println(changed);
            }
        });
        try {
            list.startDownload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
