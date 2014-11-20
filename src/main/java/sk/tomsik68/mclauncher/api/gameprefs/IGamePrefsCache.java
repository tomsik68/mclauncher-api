package sk.tomsik68.mclauncher.api.gameprefs;

import java.util.Map;

/**
 * GamePrefsCache holds sets of GamePrefs, which can be used. The string key is going to be profile
 *
 * @author Tomsik68
 */
public interface IGamePrefsCache {
    public void clearCache();

    public Map<String, GamePrefs> load() throws Exception;

    public void save(GamePrefs prefs) throws Exception;
}
