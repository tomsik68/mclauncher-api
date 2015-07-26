package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final class AssetIndex {
    private final boolean virtual;
    private final Set<Asset> objects = new HashSet<Asset>();
    private final String name;

    AssetIndex(String name, JSONObject json) {
        this.name = name;
        virtual = json.containsKey("virtual") && Boolean.parseBoolean(json.get("virtual").toString());
        JSONObject objsObj = (JSONObject) json.get("objects");
        for (Entry<String, Object> objectEntry : objsObj.entrySet()) {
            objects.add(new Asset((JSONObject) objectEntry.getValue(), objectEntry.getKey()));
        }
    }

    Set<Asset> getAssets() {
        return objects;
    }

    boolean isVirtual() {
        return virtual;
    }

    String getName(){ return name; }

}
