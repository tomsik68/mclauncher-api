package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.services.IServicesAvailability;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.util.HttpUtils;

public class YDLoginService implements ILoginService {
    private static UUID clientToken = UUID.randomUUID();
    private static String PASSWORD_LOGIN_URL = "https://authserver.mojang.com/authenticate";
    private String SESSION_LOGIN_URL = "https://authserver.mojang.com/refresh";

    public YDLoginService() {
    }

    @Override
    public String getBranding() {
        return "Yggdrasil";
    }

    @Override
    public ISession login(IProfile profile) throws Exception {
        YDLoginResponse response;
        if (profile instanceof LegacyProfile) {
            response = doPasswordLogin(profile);
        } else
            response = doSessionLogin(profile);
        return new YDSession(response);
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

    @Override
    public void save(File mcInstance) throws Exception {
        File file = new File(mcInstance, "launcher_profiles.json");
        JSONObject obj = new JSONObject();
        if (file.exists()) {
            obj = (JSONObject) JSONValue.parse(new FileReader(file));
            if (obj.containsKey("clientToken") && obj.get("clientToken").toString().equalsIgnoreCase(clientToken.toString()))
                return;
            file.delete();
        }
        file.createNewFile();
        obj.put("clientToken", clientToken);
        obj.writeJSONString(new FileWriter(file), JSONStyle.NO_COMPRESS);
    }

    @Override
    public void load(File mcInstance) throws Exception {
        File file = new File(mcInstance, "launcher_profiles.json");
        if (file.exists()) {
            JSONObject obj = (JSONObject) JSONValue.parse(new FileReader(file));
            clientToken = UUID.fromString(obj.get("clientToken").toString());
            System.out.println("Loaded client token: "+clientToken.toString());
        }
    }

}
