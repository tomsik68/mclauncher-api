package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;

public final class YDAuthProfile implements IProfile, IJSONSerializable {
    private final String userName;
    private final String uuid;
    private final String displayName;
    private final String userId;
    private String accessToken;
    private String profileName = "(Default)";

    private final String SKINS_ROOT = "https://skins.minecraft.net/MinecraftSkins/";

    public YDAuthProfile(String name, String displayName, String sessid, String uuid, String userId) {
        this.userName = name;
        this.accessToken = sessid;
        this.uuid = uuid;
        this.displayName = displayName;
        this.userId = userId;
    }

    YDAuthProfile(YDSession session){
        this(session.getUsername(), session.getUsername(), session.getSessionID(), session.getUUID(), session.getUserObject().id);
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

    public void setPassword(String sessionID) {
        accessToken = sessionID;
    }

    public String getUUID() {
        return uuid;
    }

    String getDisplayName() {
        return displayName;
    }

    String getProfileName() {
        return profileName;
    }

    void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    YDPartialGameProfile getYDGameProfile() {
        YDPartialGameProfile result = new YDPartialGameProfile(userName, uuid, false);
        return result;
    }

    String getUserId() {
        return userId;
    }

    @Override
    public String getSkinURL() {
        StringBuilder url = new StringBuilder(SKINS_ROOT);
        url = url.append(getName()).append(".png");
        return url.toString();
    }

    void update(ISession session) {
        setPassword(session.getSessionID());
    }

}
