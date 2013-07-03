package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.login.ISessionFactory;

public class MinecraftNETSessionFactory implements ISessionFactory {

    @Override
    public ISession createSession(String[] params) throws Exception {
        if (params == null || params.length < 1)
            throw new IllegalArgumentException("Unable to login");
        return null;
    }

}
