package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.impl.common.Platform;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class Library {
    private final String name;
    private final HashMap<String, String> natives = new HashMap<String, String>();
    private final Set<Rule> rules = new HashSet<Rule>();

    public Library(JSONObject json) {
        name = json.get("name").toString();
        if (json.containsKey("natives")) {
            JSONObject nativesObj = (JSONObject) json.get("natives");
            for (String nativeKey : nativesObj.keySet()) {
                natives.put(nativeKey, nativesObj.get(nativeKey).toString());
            }
            // ugly fix for ugly rename of macos to osx
            if (natives.containsKey("osx")) {
                natives.put("macos", natives.get("osx"));
            }
        }
        if (json.containsKey("rules")) {
            JSONArray rulz = (JSONArray) json.get("rules");
            for (int i = 0; i < rulz.size(); ++i) {
                rules.add(new Rule((JSONObject) rulz.get(i)));
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getNatives(IOperatingSystem os) {
        return natives.get(os.getMinecraftName());
    }

    public String getPath() {
        String[] split = name.split(":");
        StringBuilder result = new StringBuilder();
        result = result.append(split[0].replace('.', '/'));// net/sf/jopt-simple
        result = result.append('/').append(split[1]).append('/').append(split[2]).append('/'); // /jopt-simple/4.4/
        result = result.append(split[1]).append('-').append(split[2]); // jopt-simple-4.4
        if (!natives.isEmpty()) {
            IOperatingSystem os = Platform.getCurrentPlatform();
            String osName = os.getMinecraftName();
            result = result.append('-').append(natives.get(osName));
        }
        result = result.append(".jar");
        return result.toString();
    }

    public boolean isCompatible() {
        boolean compat = true;
        for (Rule rule : rules) {
            compat = compat && rule.allows();
        }
        return compat;
    }
}
