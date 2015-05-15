package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

final class Asset {
    private static final String RESOURCES_URL = "http://resources.download.minecraft.net/";

    private final String hash, key;
    private final int size;

    Asset(JSONObject obj, String key) {
        hash = obj.get("hash").toString();
        size = (Integer) obj.get("size");
        this.key = key;
    }

    final String getHash() {
        return hash;
    }

    final String getPreHash() {
        return hash.substring(0, 2);
    }

    final int getSize() {
        return size;
    }

    final String getUrl() {
        return RESOURCES_URL + getPreHash() + "/" + hash;
    }

    final String getKey(){ return key; }
}
