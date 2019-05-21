package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

final class AssetIndex {
    private final boolean virtual;
    private final Set<Asset> objects;
    private final String name;

    private AssetIndex(boolean virtual, String name, Set<Asset> objects) {
        this.virtual = virtual;
        this.objects = Collections.unmodifiableSet(objects);
        this.name = name;
    }

    static AssetIndex fromJson(String name, JSONObject json) {
        boolean virtual = json.containsKey("virtual") && Boolean.parseBoolean(json.get("virtual").toString());
        JSONObject objsObj = (JSONObject) json.get("objects");
        Set<Asset> objects = new HashSet<>();
        for (Entry<String, Object> objectEntry : objsObj.entrySet()) {
            objects.add(Asset.fromJson((JSONObject) objectEntry.getValue(), objectEntry.getKey()));
        }

        return new AssetIndex(virtual, name, objects);
    }

    Set<Asset> getAssets() {
        return objects;
    }

    boolean isVirtual() {
        return virtual;
    }

    String getName(){ return name; }

}
