package sk.tomsik68.mclauncher.api.login;

import sk.tomsik68.mclauncher.api.common.ILoadable;
import sk.tomsik68.mclauncher.api.common.ISaveable;
import sk.tomsik68.mclauncher.api.services.IOnlineService;

public interface ILoginService extends IOnlineService, ISaveable, ILoadable {
    public ISession login(IProfile profile) throws Exception;
}
