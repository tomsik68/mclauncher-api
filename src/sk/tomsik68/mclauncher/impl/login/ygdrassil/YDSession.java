package sk.tomsik68.mclauncher.impl.login.ygdrassil;

import sk.tomsik68.mclauncher.api.login.ISession;

public class YDSession implements ISession {
    private final String username, sessid, uuid;
    public YDSession(YDLoginResponse r){
        username = r.getSelectedProfile();
        sessid = r.getSessionID();
        uuid = r.getSelectedProfile();
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDownloadTicket() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLastVersion() {
        // TODO Auto-generated method stub
        return null;
    }

}
