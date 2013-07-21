package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.util.HashMap;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

import net.minidev.json.JSONObject;

public class Library {
    private final String name;
    private final HashMap<String, String> natives = new HashMap<String, String>();

    public Library(JSONObject json) {
        name = json.get("name").toString();
        JSONObject nativesObj = (JSONObject) json.get("natives");

    }

    public String getName() {
        return name;
    }

    public String getNatives(IOperatingSystem os) {
        return natives.get(os.getMinecraftName());
    }

    public String getDownloadURL() {
        return "wonderland";
    }
}
