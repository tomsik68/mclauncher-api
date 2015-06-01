import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfileIO;

public class TestLegacyIO {

    @Test
    public void test() {
        File dest = new File("lastlogin");
        MCLauncherAPI.log.info("Saving login info to "+dest.getAbsolutePath());
        IProfileIO io = new LegacyProfileIO(dest);
        IProfile profile = new LegacyProfile("Tomsik68", "mypassword");
        IProfile[] profiles = null;
        try {
            io.write(new IProfile[] { profile });
            profiles = io.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(profile.getName(), profiles[0].getName());
        assertEquals(profile.getPassword(), profiles[0].getPassword());
    }

}
