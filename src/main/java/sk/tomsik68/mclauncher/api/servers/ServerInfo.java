package sk.tomsik68.mclauncher.api.servers;

/**
 * Represents a server we can connect to
 *
 * @author Tomsik68
 */
public class ServerInfo {
    private final String ip, name, icon;
    private final int port;

    public ServerInfo(String ip, String name, String icon, int port) {
        this.ip = ip;
        this.name = name;
        this.port = port;
        this.icon = icon;
    }

    /**
     * @return IP of this server in form 1.2.3.4
     */
    public final String getIP() { return ip; }

    /**
     * @return port of this server(e.g. 25565)
     */
    public final int getPort() { return port; }

    /**
     * @return Name of this server
     */
    public final String getName(){ return name; }

    /**
     *
     * @return True or false depending on whether this object holds an icon of the server
     */
    public boolean hasIcon(){ return (icon != null) && (icon.length() > 0); }

    /**
     * Gets icon of this server
     * @return Byte array that contains icon as base64-encoded string
     */
    public String getIcon(){ return icon; }
}
