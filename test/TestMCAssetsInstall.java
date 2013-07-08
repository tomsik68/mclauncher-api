import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersion;
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
        MCAssetsVersionList list = new MCAssetsVersionList();
        list.addObserver(new IObserver<IVersion>() {
            private boolean installed = false;
            @Override
            public void onUpdate(IObservable<IVersion> observable, IVersion changed) {
                if(!installed){
                    installed = true;
                    System.out.println("Found version: "+changed.getDisplayName()+" installing");
                    try {
                        changed.getInstaller().install((MCAssetsVersion) changed, mc, new IProgressMonitor() {
                            
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
