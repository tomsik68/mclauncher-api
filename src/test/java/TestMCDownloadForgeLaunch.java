import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.backend.GlobalAuthenticationSystem;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

public class TestMCDownloadForgeLaunch {

    @Test
    public void test() {
        MCLauncherAPI.log.setLevel(Level.ALL);
        try {
            // finally use my minecraft credentials
            System.out.println("Logging in...");


            final ISession session = GlobalAuthenticationSystem.login(null);
            System.out.println("Success! Launching...");
            final MinecraftInstance mc = new MinecraftInstance(
                    Platform.getCurrentPlatform().getWorkingDirectory());
            final MCDownloadVersionList versionList = new MCDownloadVersionList(mc);
            IVersion toLaunch = versionList.retrieveVersionInfo("1.8-Forge11.14.3.1450");
            System.out.println(toLaunch);
            try {
                toLaunch.getInstaller().install(toLaunch, mc, null);
                List<String> launchCommand = toLaunch.getLauncher()
                        .getLaunchCommand(session, mc, null,
                                toLaunch, new ILaunchSettings() {

                                    @Override
                                    public boolean isModifyAppletOptions() {
                                        return false;
                                    }

                                    @Override
                                    public File getJavaLocation() {
                                        return null;
                                    }

                                    @Override
                                    public List<String> getJavaArguments() {
                                        return Arrays
                                                .asList("-XX:+UseConcMarkSweepGC",
                                                        "-XX:+CMSIncrementalMode",
                                                        "-XX:-UseAdaptiveSizePolicy",
                                                        "-Xmn128M");
                                    }

                                    @Override
                                    public String getInitHeap() {
                                        return "512M";
                                    }

                                    @Override
                                    public String getHeap() {
                                        return "1G";
                                    }

                                    @Override
                                    public Map<String, String> getCustomParameters() {
                                        return null;
                                    }

                                    @Override
                                    public List<String> getCommandPrefix() {
                                        return null;
                                    }
                                }, null);
                for (String cmd : launchCommand) {
                    System.out.print(cmd + " ");
                }
                System.out.println();
                ProcessBuilder pb = new ProcessBuilder(
                        launchCommand);
                pb.redirectError(new File("mcerr.log"));
                pb.redirectOutput(new File("mcout.log"));
                pb.directory(mc.getLocation());
                Process proc = pb.start();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(proc.getInputStream()));
                String line;
                while (isProcessAlive(proc)) {
                    line = br.readLine();
                    if (line != null && line.length() > 0)
                        System.out.println(line);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //versionList.startDownload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected boolean isProcessAlive(Process proc) {
        try {
            System.out.println("Process exited with error code:"
                    + proc.exitValue());
            return false;
        } catch (Exception e) {
            return true;
        }

    }

}
