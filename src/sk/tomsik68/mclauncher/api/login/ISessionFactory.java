package sk.tomsik68.mclauncher.api.login;

public interface ISessionFactory {
    public ISession createSession(String[] params) throws Exception;
}
