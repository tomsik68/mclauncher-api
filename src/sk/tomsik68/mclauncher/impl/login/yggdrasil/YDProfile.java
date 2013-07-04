package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.IProfile;

public class YDProfile implements IProfile, IJSONSerializable {
    private final String userName, accessToken, uuid, displayName;
    private String profileName = "(Default)";

    public YDProfile(String name, String displayName, String sessid, String uuid) {
        this.userName = name;
        this.accessToken = sessid;
        this.uuid = uuid;
        this.displayName = displayName;
    }

    public YDProfile(JSONObject json) {
        JSONObject authObj = (JSONObject) json.get("authentication");
        this.userName = authObj.get("username").toString();
        this.accessToken = authObj.get("accessToken").toString();
        this.uuid = authObj.get("uuid").toString();
        this.displayName = authObj.get("displayName").toString();
        profileName = json.get("name").toString();
    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        JSONObject auth = new JSONObject();
        auth.put("username", userName);
        auth.put("accessToken", accessToken);
        auth.put("uuid", uuid);
        auth.put("displayName", displayName);
        json.put("name", profileName);
        json.put("authentication", auth);
        return json;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return accessToken;
    }

    @Override
    public boolean isRemember() {
        return true;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public YDPartialGameProfile getYDGameProfile() {
        YDPartialGameProfile result = new YDPartialGameProfile(userName, uuid);
        return result;
    }

}
