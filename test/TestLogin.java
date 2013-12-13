import javax.swing.JOptionPane;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyLoginService;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;

public class TestLogin {

    @Test
    public void test() {
        String password = JOptionPane.showInputDialog("Enter your password to try the login system");
        IProfile profile = new LegacyProfile("tomsik68@gmail.com", password);
        LegacyLoginService lls = new LegacyLoginService();
        try {
            ISession session = lls.login(profile);
            System.out.println("Legacy Login: " + session.getSessionID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        YDLoginService yls = new YDLoginService();
        ISession session;
        try {
            session = yls.login(profile);
            System.out.println("YD Login: " + session.getSessionID());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
