package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.IProfile;

public class YDProfile implements IProfile, IJSONSerializable {
    private final String name, accessToken, uuid, displayName;
    private String profileName = "(Default)";

    public YDProfile(String name, String displayName, String sessid, String displayName1, String uuid) {
        this.name = name;
        this.accessToken = sessid;
        this.uuid = uuid;
        this.displayName = displayName1;
    }

    @Override
    public String toJSON() {
        JSONObject json = new JSONObject();
        JSONObject auth = new JSONObject();
        auth.put("username", name);
        auth.put("accessToken", accessToken);
        auth.put("uuid", uuid);
        auth.put("displayName", displayName);
        json.put("name", profileName);
        json.put("authentication", auth);
        return json.toJSONString(JSONStyle.MAX_COMPRESS);
    }

    @Override
    public String getName() {
        return name;
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
        YDPartialGameProfile result = new YDPartialGameProfile(name, uuid);
        return result;
    }

}
