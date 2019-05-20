package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.login.LoginException;

/**
 * Creates LegacySession object using parameters array from login.minecraft.net
 */
@Deprecated
final class LegacySessionFactory {

    ISession createSession(String[] params) throws Exception {
        if (params == null)
            throw new LoginException("Unable to login!");
        else if(params.length != 5) {
            MCLauncherAPI.log.fine("Parameter count is not 5.");
            throw new LoginException("Invalid parameters array length!");
        }
        MCLauncherAPI.log.fine("Everything looks fine. Login successful.");
        return new LegacySession(params[2], params[3], params[4], params[1], params[0]);
    }

}
