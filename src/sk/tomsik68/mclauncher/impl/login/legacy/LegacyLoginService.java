package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.services.IServicesAvailability;
import sk.tomsik68.mclauncher.util.HttpUtils;

public class LegacyLoginService implements ILoginService {
    private static final LegacySessionFactory factory = new LegacySessionFactory();
    private static final String LOGIN_URL = "https://login.minecraft.net/";

    public LegacyLoginService() {
    }

    @Override
    public String getBranding() {
        return "minecraft.net";
    }

    @Override
    public boolean isAvailable(IServicesAvailability availability) {
        return availability.isServiceAvailable("login.minecraft.net");
    }

    @Override
    public ISession login(IProfile profile) throws Exception {
        String loginResponse = HttpUtils.securePostWithKey(LOGIN_URL, LegacyLoginService.class.getResourceAsStream("minecraft.key"), "user="+profile.getName()+"&password="+profile.getPassword()+"&version=13");
        return factory.createSession(loginResponse.split(":"));
    }

}
