package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.ESessionType;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.login.ISession.Prop;

import java.util.List;

@Deprecated
final class LegacySession implements ISession {
    private final String userName, sessionID, uuid, downloadTicket, lastVersion;

    public LegacySession(String user, String sessid, String uuid, String dlTicket, String lVersion) {
        userName = user;
        sessionID = sessid;
        this.uuid = uuid;
        downloadTicket = dlTicket;
        lastVersion = lVersion;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public String getSessionID() {
        return sessionID;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    public String getDownloadTicket() {
        return downloadTicket;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    @Override
    public ESessionType getType() {
        return ESessionType.LEGACY;
    }

    @Override
    public List<Prop> getProperties() {
        return null;
    }

}
