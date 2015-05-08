package sk.tomsik68.mclauncher.impl.versions.mcdownload.assets;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;

public class Asset {
    private static final String RESOURCES_URL = "http://resources.download.minecraft.net/";

    private final String hash;
    private final int size;

    public Asset(JSONObject obj) {
        hash = obj.get("hash").toString();
        size = (Integer) obj.get("size");
    }

    public String getHash() {
        return hash;
    }

    public String getPreHash() {
        return hash.substring(0, 2);
    }

    public int getSize() {
        return size;
    }

    public String getUrl() {
        return RESOURCES_URL + getPreHash() + "/" + hash;
    }

}
