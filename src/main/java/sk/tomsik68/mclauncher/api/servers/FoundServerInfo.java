package sk.tomsik68.mclauncher.api.servers;


import java.util.Map;

/**
 * Represents information about server which was discovered on LAN network
 */
public class FoundServerInfo extends ServerInfo {
    private final IServerFinder source;
    private final Map<String, Object> info;

    public FoundServerInfo(IServerFinder sf, String ip, int port, String name, Map<String, Object> info) {
        super(ip, name, port);
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

    public Map<String, Object> getInformation() {
        return info;
    }

}
