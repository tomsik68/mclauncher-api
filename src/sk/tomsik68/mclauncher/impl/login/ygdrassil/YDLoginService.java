package sk.tomsik68.mclauncher.impl.login.ygdrassil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.common.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ILoginService;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.net.HttpUtils;
import sk.tomsik68.mclauncher.api.services.IServicesAvailability;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyLoginService;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;

public class YDLoginService implements ILoginService {
    private static UUID clientToken = UUID.randomUUID();
    private static String PASSWORD_LOGIN_URL = "https://authserver.mojang.com/authenticate";

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

    private YDLoginResponse doPasswordLogin(IProfile profile) throws Exception {
        YDPasswordLoginRequest request = new YDPasswordLoginRequest(profile.getName(), profile.getPassword(), clientToken.toString());
        String result = HttpUtils.secureJSONPost(PASSWORD_LOGIN_URL, LegacyLoginService.class.getResourceAsStream("minecraft.key"), request);
        YDLoginResponse response = new YDLoginResponse(JSONValue.parse(result));
        return response;
    }

    @Override
    public boolean isAvailable(IServicesAvailability availability) {
        return availability.isServiceAvailable("auth.mojang.com");
    }

    @Override
    public void save(IMinecraftInstance mc) throws Exception {
        File file = new File(mc.getLocation(), "launcher_profiles.json");
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
    public void load(IMinecraftInstance mc) throws Exception {
        File file = new File(mc.getLocation(), "launcher_profiles.json");
        if (file.exists()) {
            JSONObject obj = (JSONObject) JSONValue.parse(new FileReader(file));
            clientToken = UUID.fromString(obj.get("clientToken").toString());
        }
    }

}
