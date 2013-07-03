package sk.tomsik68.mclauncher.impl.login;

import sk.tomsik68.mclauncher.api.login.ISession;

public class OfflineSession implements ISession {
    private final String username;

    public OfflineSession(String user) {
        username = user;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getSessionID() {
        return "-";
    }

    @Override
    public String getUUID() {
        return "-";
    }

    @Override
    public String getDownloadTicket() {
        return "deprecated";
    }

    @Override
    public String getLastVersion() {
        return "0";
    }

}
