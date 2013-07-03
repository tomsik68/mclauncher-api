package sk.tomsik68.mclauncher.impl.login.ygdrassil;

import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;

public class YDLoginService implements ILoginService{

    @Override
    public String getBranding() {
        return "Yggdrasil";
    }

    @Override
    public ISession login(IProfile profile) throws Exception {
        
        return null;
    }

}
