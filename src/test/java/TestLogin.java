import static org.junit.Assert.*;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;

public class TestLogin {

    @Test
    public void test() {
        try {
            YDLoginService yls = new YDLoginService();
            yls.load(Platform.getCurrentPlatform().getWorkingDirectory());
            
            YDProfileIO profileIO = new YDProfileIO(Platform.getCurrentPlatform().getWorkingDirectory());
            IProfile[] profiles = profileIO.read();
            IProfile profile = profiles[0];
            
            ISession session;
            session = yls.login(profile);
            
            profiles[0] = profile;
            profileIO.write(profiles);
            System.out.println(session.getSessionID());
            
            assertTrue(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
