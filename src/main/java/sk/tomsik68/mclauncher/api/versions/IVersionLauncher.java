package sk.tomsik68.mclauncher.api.versions;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.mods.IModdingProfile;
import sk.tomsik68.mclauncher.api.servers.ServerInfo;

import java.util.List;

/**
 * Launcher can run one type of version
 *
 * @author Tomsik68
 */
public interface IVersionLauncher {
    /**
     * Launches minecraft
     *
     * @param session  Login session
     * @param mc       Target minecraft instance
     * @param server   Server to connect to
     * @param version  Version to launch
     * @param settings Launch settings
     * @param mods     Mods to load along with minecraft
     * @return Process which was created
     * @throws Exception various errors
     */
    public List<String> getLaunchCommand(ISession session, MinecraftInstance mc, ServerInfo server, IVersion version, ILaunchSettings settings, IModdingProfile mods)
            throws Exception;

}
