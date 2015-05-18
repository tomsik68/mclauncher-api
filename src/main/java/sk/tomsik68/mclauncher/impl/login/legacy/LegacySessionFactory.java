package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.login.LoginException;

/**
 * Creates LegacySession object using parameters array from login.minecraft.net
 */
final class LegacySessionFactory {

    final ISession createSession(String[] params) throws Exception {
        if (params == null)
            throw new LoginException("Unable to login!");
        else if(params.length <= 1)
            throw new LoginException(params[0]);
        else if(params.length != 5) {
            throw new LoginException("Invalid parameters array length!");
        }
        return new LegacySession(params[2], params[3], params[4], params[1], params[0]);
    }

}
