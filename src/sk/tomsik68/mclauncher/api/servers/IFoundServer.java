package sk.tomsik68.mclauncher.api.servers;

import java.util.Map;
/**
 * Represents a discovered server
 * @author Tomsik68
 *
 */
public interface IFoundServer extends ISavedServer {
    /**
     * 
     * @return ServerFinder which found the server
     */
    public IServerFinder getSource();
    /**
     * 
     * @return Map of information about the server
     */
    public Map<String, Object> getInformation();
}
