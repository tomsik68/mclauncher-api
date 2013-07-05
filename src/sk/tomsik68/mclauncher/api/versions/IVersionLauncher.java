package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;

public interface IVersionLauncher<V extends IVersion> {
    public void launch(ISession session,IMinecraftInstance mc,ISavedServer server,V version);
}
