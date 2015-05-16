import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersionList;

public class TestMCAssetsLaunch {

    @Test
    public void test() throws Exception {
        System.out.println("Logging in...");
        YDLoginService service = new YDLoginService();
        service.load(Platform.getCurrentPlatform().getWorkingDirectory());
        YDProfileIO profileIO = new YDProfileIO(Platform.getCurrentPlatform().getWorkingDirectory());
        IProfile[] profiles = profileIO.read();
        final ISession session = service.login(profiles[0]);
        profileIO.write(profiles);
        System.out.println("Success! Launching...");
        final MinecraftInstance mc = new MinecraftInstance(new File("testmc"));

        MCAssetsVersionList list = new MCAssetsVersionList();
        list.addObserver(new IObserver<IVersion>() {
            @Override
            public void onUpdate(IObservable<IVersion> observable, IVersion changed) {
                if (changed.getId().equalsIgnoreCase("11w47a")) {

                    System.out.println("Found version: " + changed.getDisplayName());
                    try {
                        List<String> launchCommand = changed.getLauncher().getLaunchCommand(session, mc, null, changed, new ILaunchSettings() {

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
                        }, null);
                        Process proc = new ProcessBuilder(launchCommand).start();
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
