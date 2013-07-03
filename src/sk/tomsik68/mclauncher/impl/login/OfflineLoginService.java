package sk.tomsik68.mclauncher.impl.login;

import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;

public class OfflineLoginService implements ILoginService{

    @Override
    public String getBranding() {
        return "offline";
    }

    @Override
    public ISession login(IProfile profile) throws Exception {
        // TODO return session with default parameters
        return null;
    }

}
