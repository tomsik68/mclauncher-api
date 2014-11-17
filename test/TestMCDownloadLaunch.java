import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.common.mc.VanillaMinecraftInstance;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.io.YDProfileIO;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersionList;

public class TestMCDownloadLaunch {

    @Test
    public void test() {

	try {
	    // finally use my minecraft credentials
	    System.out.println("Logging in...");
	    YDLoginService service = new YDLoginService();
	    service.load(Platform.getCurrentPlatform().getWorkingDirectory());
	    YDProfileIO profileIO = new YDProfileIO(Platform
		    .getCurrentPlatform().getWorkingDirectory());
	    IProfile[] profiles = profileIO.read();
	    /*
	     * for (IProfile profile : profiles) {
	     * System.out.println(profile.getName() + ";" +
	     * profile.getPassword()); }
	     */
	    final ISession session = service.login(profiles[0]);
	    profileIO.write(profiles);
	    System.out.println("Success! Launching...");
	    final IMinecraftInstance mc = new VanillaMinecraftInstance(
		    new File("testmc"));
	    MCDownloadVersionList versionList = new MCDownloadVersionList();
	    versionList.addObserver(new IObserver<IVersion>() {

		private boolean launched = false;

		@Override
		public void onUpdate(IObservable<IVersion> observable,
			IVersion changed) {
		    if (!launched) {
			launched = true;
			try {
			    List<String> launchCommand = changed.getLauncher()
				    .getLaunchCommand(session, mc, null,
					    changed, new ILaunchSettings() {

						@Override
						public boolean isModifyAppletOptions() {
						    return false;
						}

						@Override
						public boolean isErrorStreamRedirected() {
						    return true;
						}

						@Override
						public File getJavaLocation() {
						    return null;
						}

						@Override
						public List<String> getJavaArguments() {
						    return null;
						}

						@Override
						public String getInitHeap() {
						    return null;
						}

						@Override
						public String getHeap() {
						    return null;
						}

						@Override
						public Map<String, String> getCustomParameters() {
						    return null;
						}

						@Override
						public List<String> getCommandPrefix() {
						    return null;
						}
					    });
			    for (String cmd : launchCommand) {
				System.out.print(cmd + " ");
			    }
			    ProcessBuilder pb = new ProcessBuilder(
				    launchCommand);
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

		    }
		}
	    });
	    versionList.startDownload();
	} catch (Exception e) {
	    e.printStackTrace();
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
