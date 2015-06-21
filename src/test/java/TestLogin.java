import static org.junit.Assert.*;

import org.junit.Test;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;
import sk.tomsik68.mclauncher.util.auth.MinecraftAuthentication;

public class TestLogin {

    @Test
    public void test() {
        try {
            ISession session = MinecraftAuthentication.login(null);
            MCLauncherAPI.log.info("Login successful!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
