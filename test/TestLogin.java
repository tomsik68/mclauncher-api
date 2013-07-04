import org.junit.Test;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyLoginService;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfile;


public class TestLogin {

    @Test
    public void test() {
        IProfile profile = new LegacyProfile("Tomsik68@gmail.com", "blahblahblah");
        LegacyLoginService lls = new LegacyLoginService();
        try {
            ISession session = lls.login(profile);
            System.out.println("Legacy Login: "+session.getSessionID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        YDLoginService yls = new YDLoginService();
        ISession session = null;
        try {
             session = yls.login(profile);
            System.out.println("YD password login: "+session.getSessionID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            profile = new YDProfile("Tomsik68@gmail.com", "Tomsik68", session.getSessionID(), "(Default)", session.getUUID()); 
            session = yls.login(profile);
            System.out.println("YD session ID login: "+session.getSessionID());
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

}
