package sk.tomsik68.mclauncher.api.login;

import sk.tomsik68.mclauncher.api.services.IOnlineService;
/**
 * Login Service is responsible for converting IProfile to ISession.
 * It's up to developer to choose login service.
 *
 * */
public interface ILoginService extends IOnlineService {
    /**
     * Logs in using specified profile
     * @param profile - Profile to log in
     * @return {@link ISession} object if successful
     * @throws Exception - Connection failed, or LoginException if login failed
     */
    public ISession login(IProfile profile) throws Exception;

    /**
     * If it's possible, invalidates the specified session
     * @param session Session to invalidate
     * @throws Exception - Connection failed
     */
    public void logout(ISession session) throws Exception;
}
