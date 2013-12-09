package sk.tomsik68.mclauncher.impl.login;

import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.services.IServicesAvailability;

public class OfflineLoginService implements ILoginService {

    @Override
    public String getBranding() {
        return "offline";
    }


    @Override
    public boolean isAvailable(IServicesAvailability availability) {
        return true;
    }

    @Override
    public ISession login(IProfile profile) throws Exception {
        return new OfflineSession(profile.getName());
    }

}
