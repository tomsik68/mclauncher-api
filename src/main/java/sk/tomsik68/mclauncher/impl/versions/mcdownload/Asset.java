package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

/**
 * Represents a single Asset. It may be a sound file, texture, language file etc.
 */
final class Asset {
    private static final String RESOURCES_URL = "http://resources.download.minecraft.net/";

    private final String hash, key;
    private final int size;

    Asset(JSONObject obj, String key) {
        hash = obj.get("hash").toString();
        size = (Integer) obj.get("size");
        this.key = key;
    }

    /**
     *
     * @return Hash of this Asset
     */
    String getHash() {
        return hash;
    }

    /**
     *
     * @return First 2 characters of hash
     */
    String getPreHash() {
        return hash.substring(0, 2);
    }

    /**
     * Size of this Asset
     * @return Size in bytes
     */
    int getSize() {
        return size;
    }

    /**
     *
     * @return URL where this asset can be downloaded
     */
    String getUrl() {
        return RESOURCES_URL + getPreHash() + "/" + hash;
    }

    /**
     *
     * @return Key/Name of this asset in JSON structure
     */
    String getKey(){ return key; }
}
