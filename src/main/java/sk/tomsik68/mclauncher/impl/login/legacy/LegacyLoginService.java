package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.services.IServicesAvailability;
import sk.tomsik68.mclauncher.util.HttpUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Deprecated
public final class LegacyLoginService implements ILoginService {
    private static final LegacySessionFactory factory = new LegacySessionFactory();
    private static final String LOGIN_URL = "https://login.minecraft.net/";

    public LegacyLoginService() {
    }

    private static String encode(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "utf-8");
    }

    @Override
    public boolean isAvailable(IServicesAvailability availability) {
        return availability.isServiceAvailable("login.minecraft.net");
    }

    @Override
    public ISession login(IProfile profile) throws Exception {
        MCLauncherAPI.log.fine("Logging in with legacy service...");
        String loginResponse = HttpUtils.securePostWithKey(LOGIN_URL, LegacyLoginService.class.getResourceAsStream("minecraft.key"), "user="
                + encode(profile.getName()) + "&password=" + encode(profile.getPassword()) + "&version=13");
        MCLauncherAPI.log.fine("Got response! Parsing response...");
        ISession result = factory.createSession(loginResponse.split(":"));
        return result;
    }

    @Override
    public void logout(ISession session) throws Exception {
        // nothing happens, can't log out.
        MCLauncherAPI.log.fine("LegacyLoginService doesn't provide logout feature.");
    }

}
