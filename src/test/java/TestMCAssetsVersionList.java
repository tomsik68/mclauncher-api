import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersionList;


public class TestMCAssetsVersionList {

    @Test
    public void test() {
        MCAssetsVersionList list = new MCAssetsVersionList();
        list.addObserver(new IObserver<IVersion>() {
            @Override
            public void onUpdate(IObservable<IVersion> observable, IVersion changed) {
                System.out.println("Found version: "+changed.getDisplayName());
            }
        });
        try {
            list.startDownload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
