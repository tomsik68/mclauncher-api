package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.Rule.Action;
import sk.tomsik68.mclauncher.util.IExtractRules;
import sk.tomsik68.mclauncher.util.StringSubstitutor;

import java.util.ArrayList;
import java.util.HashMap;

final class Library {
    private final StringSubstitutor libraryPathSubstitutor = new StringSubstitutor("${%s}");
    private final String name;
    private final HashMap<String, String> natives = new HashMap<String, String>();
    private final ArrayList<Rule> rules = new ArrayList<Rule>();
    private LibraryExtractRules extractRules;
    private final static String LIBRARY_BASE_URL = "https://libraries.minecraft.net/";
    private String url = LIBRARY_BASE_URL;

    public Library(JSONObject json) {
        name = json.get("name").toString();
        if (json.containsKey("natives")) {
            JSONObject nativesObj = (JSONObject) json.get("natives");
            for (String nativeKey : nativesObj.keySet()) {
                String key = nativeKey;
                String value = nativesObj.get(nativeKey).toString();

                natives.put(key, value);
            }
        }
        if (json.containsKey("rules")) {
            JSONArray rulz = (JSONArray) json.get("rules");
            for (int i = 0; i < rulz.size(); ++i) {
                rules.add(new Rule((JSONObject) rulz.get(i)));
            }
        }
        if (json.containsKey("extract")) {
            extractRules = new LibraryExtractRules((JSONObject) json.get("extract"));
        }
        if (json.containsKey("url")) {
            url = json.get("url").toString();
        }
    }

    public String getName() {
        return name;
    }

    public String getNatives(IOperatingSystem os) {
        if (!natives.containsKey(os.getMinecraftName()))
            return natives.get(Platform.wrapName(os.getMinecraftName())).replace("${arch}", System.getProperty("sun.arch.data.model"));
        return natives.get(os.getMinecraftName()).replace("${arch}", os.getArchitecture());
    }

    public String getPath() {
        libraryPathSubstitutor.setVariable("arch", Platform.getCurrentPlatform().getArchitecture());
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
        return libraryPathSubstitutor.substitute(result.toString());
    }

    public boolean isCompatible() {
        Action action = Action.DISALLOW;
        for (Rule rule : rules) {
            if (rule.applies()) {
                action = rule.getAction();
                System.out.println("Rule: " + rule.toString());
            }
        }
        return rules.isEmpty()
                || (action == Action.ALLOW && (!hasNatives() || natives.containsKey(Platform.getCurrentPlatform().getMinecraftName())));
    }

    public boolean hasNatives() {
        return !natives.isEmpty();
    }

    public IExtractRules getExtractRules() {
        return extractRules;
    }

    public String getDownloadURL() {
        return url.concat(getPath());
    }
}
