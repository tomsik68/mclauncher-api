package sk.tomsik68.mclauncher.api.login;

public interface ISession {
    public String getUsername();

    public String getSessionID();

    public String getUUID();

    public String getDownloadTicket();

    public String getLastVersion();

}
