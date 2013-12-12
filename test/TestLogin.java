import org.junit.Test;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyLoginService;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.io.YDProfileIO;

public class TestLogin {

    @Test
    public void test() {
        IProfile profile = new LegacyProfile("Tomsik68@gmail.com", "");
        LegacyLoginService lls = new LegacyLoginService();
        try {
            ISession session = lls.login(profile);
            System.out.println("Legacy Login: " + session.getSessionID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        YDProfileIO ypi = new YDProfileIO(Platform.getCurrentPlatform().getWorkingDirectory());
        try {
            IProfile[] profiles = ypi.read();
            for (IProfile p : profiles) {
                System.out.println(p.getName()+" "+p.getPassword());
            }
        } catch (Exception e) {
        }

    }

}
