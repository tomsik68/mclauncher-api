package sk.tomsik68.mclauncher.api.servers;

import java.io.IOException;

/**
 * Player-saved server storage
 *
 * @author Tomsik68
 */
public interface IServerStorage {
    /**
     * @return Array of {@link ServerInfo} which are all servers player has saved
     */
    public ServerInfo[] loadServers() throws Exception;

    /**
     * Overwrites servers in this storage by provided array
     * */
    public void saveServers(ServerInfo[] servers) throws Exception;
}
