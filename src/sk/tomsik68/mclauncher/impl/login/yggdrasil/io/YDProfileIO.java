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
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfile;

public class YDProfileIO implements IProfileIO, IGamePrefsCache {
    private final File dest;
    private String selected = "(Default)";

    public YDProfileIO(File mcInstance) {
        dest = new File(mcInstance, "launcher_profiles.json");
    }

    @Override
    public IProfile[] read() throws Exception {
        JSONObject root = (JSONObject) JSONValue.parse(new FileReader(dest));

        JSONObject profiles = (JSONObject) root.get("profiles");
        IProfile[] result = new IProfile[profiles.size()];
        int i = 0;
        for (String key : profiles.keySet()) {
            result[i] = new YDProfile((JSONObject) profiles.get(key));
            ++i;
        }
        return result;
    }

    @Override
    public void write(IProfile[] profiles) throws Exception {
        if (!dest.exists())
            dest.createNewFile();
        JSONObject jRoot = new JSONObject();
        JSONObject jProfiles = new JSONObject();
        for (IProfile profile : profiles) {
            jProfiles.put(((YDProfile) profile).getProfileName(), ((IJSONSerializable) profile).toJSON());
        }
        jRoot.put("selectedProfile", selected);
        jRoot.put("profiles", jProfiles);
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
        return null;
    }

    @Override
    public void save(GamePrefs prefs) throws Exception {

    }

}
