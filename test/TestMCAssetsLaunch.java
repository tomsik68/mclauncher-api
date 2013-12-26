import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersion;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersionList;

public class TestMCAssetsLaunch {

    @Test
    public void test() {
        final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));
        MCAssetsVersionList list = new MCAssetsVersionList();
        list.addObserver(new IObserver<IVersion>() {
            @Override
            public void onUpdate(IObservable<IVersion> observable, IVersion changed) {
                if (changed.getId().equalsIgnoreCase("11w47a")) {
                    IProfile profile = new LegacyProfile("Tomsik68@gmail.com", "mypassword");
                    ILoginService lls = new YDLoginService();
                    ISession session = null;
                    try {
                        session = lls.login(profile);
                        System.out.println("Login: " + session.getSessionID());
                    } catch (Exception e) {
                        e.printStackTrace();
                        fail(e.getMessage());
                    }
                    System.out.println("Found version: " + changed.getDisplayName());
                    try {
                        Process proc = changed.getLauncher().launch(session, mc, null, (MCAssetsVersion) changed, new ILaunchSettings() {

                            @Override
                            public boolean isModifyAppletOptions() {
                                return false;
                            }

                            @Override
                            public boolean isErrorStreamRedirected() {
                                return true;
                            }

                            @Override
                            public String getInitHeap() {
                                return "2G";
                            }

                            @Override
                            public String getHeap() {
                                return "3G";
                            }

                            @Override
                            public Map<String, String> getCustomParameters() {
                                return null;
                            }

                            @Override
                            public List<String> getCommandPrefix() {
                                return Collections.emptyList();
                            }

                            @Override
                            public File getJavaLocation() {
                                return null;
                            }

                            @Override
                            public List<String> getJavaArguments() {
                                return null;
                            }
                        });
                        BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        String line;
                        while (isProcessAlive(proc)) {
                            line = br.readLine();
                            if (line != null && line.length() > 0)
                                System.out.println(line);
                        }
                    } catch (Exception e) {
                        fail(e.getMessage());
                        e.printStackTrace();
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

    protected boolean isProcessAlive(Process proc) {
        try {
            proc.exitValue();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

}
