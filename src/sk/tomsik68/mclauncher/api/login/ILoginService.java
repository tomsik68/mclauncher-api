package sk.tomsik68.mclauncher.api.login;

import sk.tomsik68.mclauncher.api.common.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.services.IService;

public interface ILoginService extends IService {
    public ISession login(IProfile profile) throws Exception;

    public void save(IMinecraftInstance mc) throws Exception;

    public void load(IMinecraftInstance mc) throws Exception;
}
