import org.junit.Test;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.io.YDProfileIO;

public class TestLogin {

    @Test
    public void test() {
        /*String password = JOptionPane.showInputDialog("Enter your password to try the login system");
        IProfile profile = new LegacyProfile("tomsik68@gmail.com", password);
        LegacyLoginService lls = new LegacyLoginService();
        try {
            ISession session = lls.login(profile);
            System.out.println("Legacy Login: " + session.getSessionID());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
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
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
