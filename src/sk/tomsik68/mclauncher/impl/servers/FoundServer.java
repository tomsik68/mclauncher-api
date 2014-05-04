package sk.tomsik68.mclauncher.impl.servers;

import java.util.HashMap;
import java.util.Map;

import sk.tomsik68.mclauncher.api.servers.IFoundServer;
import sk.tomsik68.mclauncher.api.servers.IServerFinder;

public class FoundServer implements IFoundServer {
    private static final int DEFAULT_PORT = 25565;
    private final IServerFinder source;
    private HashMap<String, Object> info = new HashMap<String, Object>();
    private String ip, name;
    private int port;

    public FoundServer(IServerFinder sf, String ip, String name) {
        source = sf;
        this.ip = ip;
        if (ip.contains(":")) {
            this.ip = ip.split(":")[0];
            this.port = Integer.parseInt(ip.split(":")[1]);
        } else {
            this.port = DEFAULT_PORT;
        }

        this.name = name;
    }

    @Override
    public String getIP() {
        return ip;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IServerFinder getSource() {
        return source;
    }

    @Override
    public Map<String, Object> getInformation() {
        return info;
    }

    @Override
    public int getPort() {
        return port;
    }

}
