package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import sk.tomsik68.mclauncher.api.login.ISession;

public class YDSession implements ISession {
    private final String username, sessid, uuid;
    public YDSession(YDLoginResponse r){
        username = r.getSelectedProfile().getName();
        sessid = r.getSessionID();
        uuid = r.getSelectedProfile().getId();
        
    }
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getSessionID() {
        return sessid;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public String getDownloadTicket() {
        return "deprecated";
    }

    @Override
    public String getLastVersion() {
        return "deprecated";
    }

}
