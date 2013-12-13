package sk.tomsik68.mclauncher.impl.login.yggdrasil.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDAuthProfile;

public class YDProfileIO implements IProfileIO {
    private final File dest;

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
        JSONObject jRoot, authDb;
        if (!dest.exists()) {
            dest.createNewFile();
            jRoot = new JSONObject();
            authDb = new JSONObject();
        } else {
            jRoot = (JSONObject) JSONValue.parse(new FileInputStream(dest));
            authDb = (JSONObject) jRoot.get("authenticationDatabase");
        }

        for (IProfile profile : profiles) {
            if(!(profile instanceof YDAuthProfile))
                throw new IllegalArgumentException("You can only save YDAuthProfile with this system!");
            authDb.put(((YDAuthProfile) profile).getUuid(), ((IJSONSerializable) profile).toJSON());
        }
        jRoot.put("authenticationDatabase", authDb);
        FileWriter fw = new FileWriter(dest);
        jRoot.writeJSONString(fw, JSONStyle.NO_COMPRESS);
        fw.flush();
        fw.close();
    }

}
