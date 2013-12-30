package sk.tomsik68.mclauncher.api.login;

import java.util.List;

import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDUserObject.Prop;

public interface ISession {
    public String getUsername();

    public String getSessionID();

    public String getUUID();

    public ESessionType getType();

    public List<Prop> getProperties();
}
