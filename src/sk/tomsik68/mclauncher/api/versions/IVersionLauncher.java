package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;

public interface IVersionLauncher {

    public Process launch(ISession session, IMinecraftInstance mc, ISavedServer server, IVersion version, ILaunchSettings settings) throws Exception;
}
