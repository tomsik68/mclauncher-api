package sk.tomsik68.mclauncher.api.login;

import sk.tomsik68.mclauncher.api.services.IService;

public interface ILoginService extends IService {
    public ISession login(IProfile profile) throws Exception;

}
