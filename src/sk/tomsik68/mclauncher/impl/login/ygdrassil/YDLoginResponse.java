package sk.tomsik68.mclauncher.impl.login.ygdrassil;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class YDLoginResponse extends YDResponse {
    private final String sessionID, clientToken, selectedProfile;
    private YDGameProfile[] availableProfiles;

    public YDLoginResponse(JSONObject json) {
        super(json);
        sessionID = json.get("accessToken").toString();
        clientToken = json.get("clientToken").toString();
        selectedProfile = json.get("selectedProfile").toString();
        JSONArray profiles = (JSONArray) json.get("availableProfiles");
        availableProfiles = new YDGameProfile[profiles.size()];
        int i = 0;
        for (Object object : profiles) {
            JSONObject jsonObj = (JSONObject) object;
            availableProfiles[i] = new YDGameProfile(jsonObj);
            i++;
        }
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getClientToken() {
        return clientToken;
    }

    public String getSelectedProfile() {
        return selectedProfile;
    }

    public YDGameProfile getProfile(int index) {
        return availableProfiles[index];
    }
}
