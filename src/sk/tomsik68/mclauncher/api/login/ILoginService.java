package sk.tomsik68.mclauncher.api.login;

import sk.tomsik68.mclauncher.api.common.ILoadable;
import sk.tomsik68.mclauncher.api.common.ISaveable;
import sk.tomsik68.mclauncher.api.services.IService;

public interface ILoginService extends IService, ISaveable, ILoadable {
    public ISession login(IProfile profile) throws Exception;


    
}
