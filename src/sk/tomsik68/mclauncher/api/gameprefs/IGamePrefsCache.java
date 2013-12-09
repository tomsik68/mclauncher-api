package sk.tomsik68.mclauncher.api.gameprefs;

import java.util.Map;

public interface IGamePrefsCache {
    public void clearCache();

    public Map<String, GamePrefs> load() throws Exception;

    public void save(GamePrefs prefs) throws Exception;
}
