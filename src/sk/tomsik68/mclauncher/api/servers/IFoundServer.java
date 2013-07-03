package sk.tomsik68.mclauncher.api.servers;

import java.util.Map;

public interface IFoundServer extends ISavedServer {
    public IServerFinder getSource();

    public Map<String, Object> getInformation();
}
