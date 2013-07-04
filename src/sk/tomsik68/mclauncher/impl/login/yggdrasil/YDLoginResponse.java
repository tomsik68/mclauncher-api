package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import java.util.HashMap;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class YDLoginResponse extends YDResponse {
    private final String sessionID, clientToken;
    private final YDPartialGameProfile selectedProfile;
    private HashMap<String, YDPartialGameProfile> profiles = new HashMap<String, YDPartialGameProfile>();

    public YDLoginResponse(JSONObject json) {
        super(json);
        sessionID = json.get("accessToken").toString();
        clientToken = json.get("clientToken").toString();
        selectedProfile = new YDPartialGameProfile((JSONObject) json.get("selectedProfile"));
        JSONArray profiles = (JSONArray) json.get("availableProfiles");
        if (profiles != null) {
            for (Object object : profiles) {
                JSONObject jsonObj = (JSONObject) object;
                YDPartialGameProfile p = new YDPartialGameProfile(jsonObj);
                this.profiles.put(p.getName(), p);
            }
        }
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getClientToken() {
        return clientToken;
    }

    public YDPartialGameProfile getSelectedProfile() {
        return selectedProfile;
    }

    public YDPartialGameProfile getProfile(String name) {
        return profiles.get(name);
    }
}
