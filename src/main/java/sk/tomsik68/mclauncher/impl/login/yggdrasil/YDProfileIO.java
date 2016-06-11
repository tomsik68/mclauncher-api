package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDAuthProfile;
import sk.tomsik68.mclauncher.util.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;

public final class YDProfileIO implements IProfileIO {
    private final File dest;

    public YDProfileIO(File mcInstance) {
        dest = new File(mcInstance, "launcher_profiles.json");
    }

    @Override
    public IProfile[] read() throws Exception {
        FileReader fileReader = new FileReader(dest);
        JSONObject root = (JSONObject) JSONValue.parse(fileReader);
        fileReader.close();

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
            FileUtils.createFileSafely(dest);
            jRoot = new JSONObject();
            authDb = new JSONObject();
        } else {
            MCLauncherAPI.log.fine("Existing profile storage file found. Loading profiles in case they would be overwritten.");
            FileReader fileReader = new FileReader(dest);
            jRoot = (JSONObject) JSONValue.parse(fileReader);
            fileReader.close();
            authDb = (JSONObject) jRoot.get("authenticationDatabase");
            if(authDb == null){
                authDb = new JSONObject();
            }
        }

        for (IProfile p : profiles) {
            if (!(p instanceof YDAuthProfile))
                throw new IllegalArgumentException("You can only save YDAuthProfile with this system!");
            YDAuthProfile profile = (YDAuthProfile)p;
            authDb.put(profile.getUUID().replace("-", ""), profile.toJSON());
        }
        jRoot.put("authenticationDatabase", authDb);
        FileWriter fw = new FileWriter(dest);
        jRoot.writeJSONString(fw, JSONStyle.NO_COMPRESS);
        fw.flush();
        fw.close();
    }

}
