package sk.tomsik68.mclauncher.impl.login.yggdrasil.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.gameprefs.GamePrefs;
import sk.tomsik68.mclauncher.api.gameprefs.IGamePrefsCache;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDAuthProfile;

public class YDProfileIO implements IProfileIO, IGamePrefsCache {
    private final File dest;
    private String selected = "(Default)";

    public YDProfileIO(File mcInstance) {
        dest = new File(mcInstance, "launcher_profiles.json");
    }

    @Override
    public IProfile[] read() throws Exception {
        JSONObject root = (JSONObject) JSONValue.parse(new FileReader(dest));

        JSONObject authDatabase = (JSONObject) root.get("authenticationDatabase");
        IProfile[] result = new IProfile[authDatabase.size()];
        int i = 0;
        for (String key : authDatabase.keySet()) {
            result[i] = new YDAuthProfile((JSONObject) authDatabase.get(key));
            ++i;
        }
        return result;
    }

    @Override
    public void write(IProfile[] profiles) throws Exception {
        if (!dest.exists())
            dest.createNewFile();
        JSONObject jRoot = new JSONObject();
        JSONObject authDb = new JSONObject();
        for (IProfile profile : profiles) {
            authDb.put(((YDAuthProfile) profile).getUuid(), ((IJSONSerializable) profile).toJSON());
        }
        jRoot.put("selectedProfile", selected);
        jRoot.put("authenticationDatabase", authDb);
        FileWriter fw = new FileWriter(dest);
        jRoot.writeJSONString(fw, JSONStyle.NO_COMPRESS);
        fw.flush();
        fw.close();
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    @Override
    public void clearCache() {

    }

    @Override
    public Map<String, GamePrefs> load() throws Exception {
        // TODO
        return null;
    }

    @Override
    public void save(GamePrefs prefs) throws Exception {
        // TODO
    }

}
