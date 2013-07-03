package sk.tomsik68.mclauncher.impl.login.ygdrassil;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;

public class YDGameProfile implements IJSONSerializable {
    private final String name, id;

    public YDGameProfile(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public YDGameProfile(JSONObject jsonObj) {
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
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("name", name);
        return obj.toJSONString(JSONStyle.MAX_COMPRESS);
    }
}
