package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;

public class YDAuthProfile implements IProfile, IJSONSerializable {
    private final String userName;
    private String accessToken;
    private final String uuid;
    private final String displayName;
    private final String userId;
    private String profileName = "(Default)";

    public YDAuthProfile(String name, String displayName, String sessid, String uuid, String userId) {
        this.userName = name;
        this.accessToken = sessid;
        this.uuid = uuid;
        this.displayName = displayName;
        this.userId = userId;
    }

    public YDAuthProfile(JSONObject json) {
        this.userName = json.get("username").toString();
        this.accessToken = json.get("accessToken").toString();
        this.uuid = json.get("uuid").toString();
        this.displayName = json.get("displayName").toString();
        this.userId = json.get("userid").toString();

    }

    @Override
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("username", userName);
        json.put("accessToken", accessToken);
        json.put("uuid", uuid);
        json.put("userid", userId);
        json.put("displayName", displayName);
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
        YDPartialGameProfile result = new YDPartialGameProfile(userName, uuid, false);
        return result;
    }

    public String getUserId() {
        return userId;
    }

    public void setPassword(String sessionID) {
        accessToken = sessionID;
    }

    @Override
    public void update(ISession session) {
        setPassword(session.getSessionID());
    }

}
