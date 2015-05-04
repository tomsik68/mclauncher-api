package sk.tomsik68.mclauncher.api.servers;

/**
 * Represents a server which was saved in the data file
 *
 * @author Tomsik68
 */
public class ServerInfo {
    private final String ip, name;
    private final int port;

    public ServerInfo(String ip, String name, int port) {
        this.ip = ip;
        this.name = name;
        this.port = port;
    }

    /**
     * @return IP of this server in form 1.2.3.4
     */
    public String getIP() {return ip;}

    /**
     * @return port of this server(e.g. 25565)
     */
    public int getPort() {return port; }

    /**
     * @return Name of this server
     */
    public String getName(){ return name; }
}
