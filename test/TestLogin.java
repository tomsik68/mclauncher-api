import static org.junit.Assert.*;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyLoginService;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;


public class TestLogin {

    @Test
    public void test() {
        LegacyProfile profile = new LegacyProfile("Tomsik68@gmail.com", "mypassword");
        LegacyLoginService lls = new LegacyLoginService();
        try {
            ISession session = lls.login(profile);
            System.out.println(session.getSessionID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
