package sk.tomsik68.mclauncher.api.servers;
/**
 * Player-saved server storage
 * @author Tomsik68
 *
 */
public interface IServerStorage {
    /**
     * 
     * @return Array of {@link ISavedServer} which are all servers player has saved
     */
    public ISavedServer[] loadServers();
}
