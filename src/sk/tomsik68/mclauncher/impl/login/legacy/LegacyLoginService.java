package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.login.ISessionFactory;

public class LegacyLoginService implements ILoginService {
    private final ISessionFactory factory;

    public LegacyLoginService() {
        factory = new MinecraftNETSessionFactory();
    }

    @Override
    public ISession login(IProfile profile) throws Exception {
        // create connection and retrieve the array which represents a session
        String[] result = null;
        // create session from the array or throw an exception(use factory)
        return factory.createSession(result);
    }

    @Override
    public String getBranding() {
        return "minecraft.net";
    }

}
