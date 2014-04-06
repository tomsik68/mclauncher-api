package sk.tomsik68.mclauncher.api.login;
/**
 * This is for old login system to deal with the params array it receives from server. 
 * @author Tomsik68
 *
 */
public interface ISessionFactory {
    /**
     * Creates session from specified params array
     * @param params
     * @return Session from specified parameters
     * @throws Exception Bad password, network error etc.
     */
    public ISession createSession(String[] params) throws Exception;
}
