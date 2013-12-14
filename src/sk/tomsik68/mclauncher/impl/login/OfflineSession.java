package sk.tomsik68.mclauncher.impl.login;

import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacySession;

public class OfflineSession extends LegacySession implements ISession {

    public OfflineSession(String user) {
        super(user, "-", "", "deprecated", "deprecated");
    }

}
