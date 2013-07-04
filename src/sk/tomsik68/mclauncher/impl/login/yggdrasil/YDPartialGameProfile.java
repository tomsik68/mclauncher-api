package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;

public class YDPartialGameProfile implements IJSONSerializable {
    private final String name, id;

    public YDPartialGameProfile(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public YDPartialGameProfile(JSONObject jsonObj) {
        this(jsonObj.get("name").toString(),jsonObj.get("id").toString());
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toJSON() {
        return toJSONObject().toJSONString(JSONStyle.MAX_COMPRESS);
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("name", name);
        return obj;
    }
}
