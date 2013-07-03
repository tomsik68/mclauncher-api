package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.ISession;

public class LegacySession implements ISession {
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

    @Override
    public String getDownloadTicket() {
        return downloadTicket;
    }

    @Override
    public String getLastVersion() {
        return lastVersion;
    }

}
