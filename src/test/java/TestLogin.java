import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.backend.GlobalAuthenticationSystem;

public class TestLogin {

    @Test
    public void test() {
        try {
            ISession session = GlobalAuthenticationSystem.login(null);
            MCLauncherAPI.log.info("Login successful!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
