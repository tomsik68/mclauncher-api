package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;
import sk.tomsik68.mclauncher.impl.versions.mcassets.MCAssetsVersion;

public interface IVersionLauncher<V extends IVersion> {

    public Process launch(ISession session, IMinecraftInstance mc, ISavedServer server, MCAssetsVersion version, ILaunchSettings settings) throws Exception;
}
