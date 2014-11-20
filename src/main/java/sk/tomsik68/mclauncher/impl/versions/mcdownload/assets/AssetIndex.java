package sk.tomsik68.mclauncher.impl.versions.mcdownload.assets;

import net.minidev.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AssetIndex {
    private final boolean virtual;
    private final HashMap<String, Asset> objects = new HashMap<String, Asset>();

    public AssetIndex(JSONObject json) {
        virtual = json.containsKey("virtual") && Boolean.parseBoolean(json.get("virtual").toString());
        JSONObject objsObj = (JSONObject) json.get("objects");
        for (Entry<String, Object> objectEntry : objsObj.entrySet()) {
            objects.put(objectEntry.getKey(), new Asset((JSONObject) objectEntry.getValue()));
        }
    }

    public Map<String, Asset> getAssets() {
        return objects;
    }

    public boolean isVirtual() {
        return virtual;
    }

}
