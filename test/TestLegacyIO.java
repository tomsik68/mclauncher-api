import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.legacy.io.LegacyProfileIO;

public class TestLegacyIO {

    @Test
    public void test() {
        IProfileIO io = new LegacyProfileIO(new File("savetest"));
        IProfile profile = new LegacyProfile("Tomsik68", "mypassword");
        IProfile[] profiles = null;
        try {
            io.write(new IProfile[] { profile });
            profiles = io.read();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        assertEquals(profile.getName(), profiles[0].getName());
        assertEquals(profile.getPassword(), profiles[0].getPassword());
    }

}
