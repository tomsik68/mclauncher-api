package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.services.IServicesAvailability;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.util.HttpUtils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;

public final class YDLoginService implements ILoginService {
    public static UUID clientToken = UUID.randomUUID();
    private static final String PASSWORD_LOGIN_URL = "https://authserver.mojang.com/authenticate";
    private static final String SESSION_LOGIN_URL = "https://authserver.mojang.com/refresh";
    private static final String SESSION_LOGOUT_URL = "https://authserver.mojang.com/invalidate";

    public YDLoginService() {
    }

    @Override
    public ISession login(IProfile profile) throws Exception {
        YDLoginResponse response;
        if (profile instanceof LegacyProfile)
            response = doPasswordLogin(profile);
        else {
            response = doSessionLogin(profile);
        }

        YDSession result = new YDSession(response);
        profile.update(result);
        return result;
    }

    private YDLoginResponse doSessionLogin(IProfile profile) throws Exception {
        YDSessionLoginRequest request = new YDSessionLoginRequest(profile.getPassword(), clientToken.toString());
        String result = HttpUtils.doJSONPost(SESSION_LOGIN_URL, request);
        YDLoginResponse response = new YDLoginResponse((JSONObject) JSONValue.parse(result));
        return response;
    }

    private YDLoginResponse doPasswordLogin(IProfile profile) throws Exception {
        YDPasswordLoginRequest request = new YDPasswordLoginRequest(profile.getName(), profile.getPassword(), clientToken.toString());
        String result = HttpUtils.doJSONPost(PASSWORD_LOGIN_URL, request);
        YDLoginResponse response = new YDLoginResponse((JSONObject) JSONValue.parse(result));
        return response;
    }

    @Override
    public boolean isAvailable(IServicesAvailability availability) {
        return availability.isServiceAvailable("auth.mojang.com");
    }

    public void save(File mcInstance) throws Exception {
        File file = new File(mcInstance, "launcher_profiles.json");
        saveTo(file);
    }

    public void saveTo(File file) throws Exception {
        JSONObject obj = new JSONObject();
        if (file.exists()) {
            try {
                obj = (JSONObject) JSONValue.parse(new FileReader(file));
                if (obj.containsKey("clientToken"))
                    return;
            } catch (Exception e) {

            }
            file.delete();

        }
        // file.createNewFile();
        obj.put("clientToken", clientToken.toString());
        FileWriter fw = new FileWriter(file);
        obj.writeJSONString(fw, JSONStyle.NO_COMPRESS);
        fw.flush();
        fw.close();
    }

    public void load(File mcInstance) throws Exception {
        File file = new File(mcInstance, "launcher_profiles.json");
        loadFrom(file);
    }

    public void loadFrom(File file) throws Exception {
        if (file.exists()) {
            JSONObject obj = (JSONObject) JSONValue.parse(new FileReader(file));
            clientToken = UUID.fromString(obj.get("clientToken").toString());
            //MCLauncherAPI.log.info("Loaded client token: " + clientToken.toString());
        }
    }

    @Override
    public void logout(ISession session) throws Exception {
        YDLogoutRequest request = new YDLogoutRequest(session, clientToken);
        String result = HttpUtils.doJSONPost(SESSION_LOGOUT_URL, request);
        if (result.length() > 0) {
            YDResponse response = new YDResponse((JSONObject) JSONValue.parse(result));
            if (response.getError() != null) {
                throw new RuntimeException(response.getError() + " " + response.getMessage());
            }
        }
    }

}
