package sk.tomsik68.mclauncher.api.login;

import sk.tomsik68.mclauncher.api.services.IOnlineService;

public interface ILoginService extends IOnlineService {
    public ISession login(IProfile profile) throws Exception;

    public void logout(ISession session) throws Exception;
}
