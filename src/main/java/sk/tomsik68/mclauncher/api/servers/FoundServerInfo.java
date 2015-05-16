package sk.tomsik68.mclauncher.api.servers;


import java.util.Map;

/**
 * Represents information about server which was discovered on LAN network
 */
public class FoundServerInfo extends ServerInfo {
    private final IServerFinder source;
    private final Map<String, Object> info;

    public FoundServerInfo(IServerFinder sf, String ip, String icon, int port, String name, Map<String, Object> info) {
        super(ip, name,icon, port);
        source = sf;
        this.info = info;
    }

    /**
     *
     * @return {@link IServerFinder} that found this server
     */
    public IServerFinder getSource() {
        return source;
    }

    /**
     *
     * @return {@link Map} of Information about found server. Each server finder implementation may put something else into the map. There's no guidelines or limits on what should be in there...
     */
    public Map<String, Object> getInformation() {
        return info;
    }

}
