package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

final class YDLoginResponse extends YDResponse {
    @Nullable
    private final String sessionID, clientToken;
    @Nullable
    private final YDPartialGameProfile selectedProfile;
    private HashMap<String, YDPartialGameProfile> profiles = new HashMap<String, YDPartialGameProfile>();
    private YDUserObject user;

    public YDLoginResponse(JSONObject json) {
        super(json);
        if (getError() != null) {
            sessionID = null;
            clientToken = null;
            selectedProfile = null;
            return;
        }
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
        if (json.containsKey("user"))
            user = new YDUserObject((JSONObject) json.get("user"));
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

    public YDUserObject getUserObject() {
        if (user == null) {
            user = new YDUserObject(selectedProfile.getName());
        }
        return user;
    }
}
