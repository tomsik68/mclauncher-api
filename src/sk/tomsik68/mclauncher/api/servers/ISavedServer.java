package sk.tomsik68.mclauncher.api.servers;
/**
 * Represents a server which was saved in the data file
 * @author Tomsik68
 *
 */
public interface ISavedServer {
    /**
     * 
     * @return IP of this server in form 1.2.3.4:5678
     */
    public String getIP();
    /**
     * 
     * @return Name of this server
     */
    public String getName();
}
