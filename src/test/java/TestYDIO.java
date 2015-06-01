import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDAuthProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;

public class TestYDIO {

    @Test
    public void test() {
        YDProfileIO io = new YDProfileIO(new File("testmc"));
        YDAuthProfile profile = new YDAuthProfile("Tomsik68@gmail.com", "Tomsik68", "blahblahblahsessionID", "blahblahblahblahyuuid", "blahblahblahuserid");
        IProfile[] profiles = null;
        try {
            io.write(new IProfile[] { profile });
            profiles = io.read();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(profile.getPassword(), profiles[0].getPassword());
        assertEquals(profile.getName(), profiles[0].getName());
    }

}
