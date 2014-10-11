package sk.tomsik68.mclauncher.api.versions;

import java.util.List;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;

/**
 * Launcher can run one type of version
 * 
 * @author Tomsik68
 * 
 */
public interface IVersionLauncher {
    /**
     * Launches minecraft
     * 
     * @param session
     *            Login session
     * @param mc
     *            Target minecraft instance
     * @param server
     *            Server to connect to
     * @param version
     *            Version to launch
     * @param settings
     *            Launch settings
     * @return Process which was created
     * @throws Exception
     *             various errors
     */
    public List<String> getLaunchCommand(ISession session, IMinecraftInstance mc, ISavedServer server, IVersion version, ILaunchSettings settings)
            throws Exception;

}
