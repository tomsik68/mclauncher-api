package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.IExtractRules;
import sk.tomsik68.mclauncher.util.StringSubstitutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a library that is needed to run minecraft.
 */
final class Library {
    private final StringSubstitutor libraryPathSubstitutor = new StringSubstitutor("${%s}");
    private String name;
    private final Map<String, String> natives = new HashMap<String, String>();
    private RuleList rules;
    private LibraryExtractRules extractRules;
    private Artifact artifact;
    private Map<String, Artifact> classifiers = new HashMap<>();

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
        rules = RuleList.fromJson((JSONArray) json.get("rules"));
        if (json.containsKey("extract")) {
            extractRules = new LibraryExtractRules((JSONObject) json.get("extract"));
        } else {
            extractRules = null;
        }
        JSONObject downloads = (JSONObject) json.get("downloads");
        artifact = Artifact.fromJson((JSONObject) downloads.get("artifact"));
        if (downloads.containsKey("classifiers")) {
            JSONObject cls = (JSONObject) downloads.get("classifiers");
            assert cls != null;
            for (Map.Entry<String, Object> entry : cls.entrySet()) {
                classifiers.put(entry.getKey(), Artifact.fromJson((JSONObject)entry.getValue()));
            }
        }
    }

    public String getName() {
        return name;
    }

    /**
     * Returns name of library that holds natives for given operating system
     * @param os - IOperatingSystem to check
     * @return Name of library which holds natives for given OS
     */
    public Artifact getNatives(IOperatingSystem os) {
        String k = null;
        if (natives.containsKey(os.getMinecraftName()))
            k = natives.get(os.getMinecraftName()).replace("${arch}", os.getArchitecture());
        else
            k = natives.get(Platform.wrapName(os.getMinecraftName())).replace("${arch}", os.getArchitecture());
        return classifiers.get(k);
    }

    /**
     * Returns relative path of library as string. Relative path is used in URLs, file paths.
     * You can read more about this on wiki...
     * @return Relative path of library.
     */
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
            if(!natives.containsKey(osName))
                osName = Platform.wrapName(osName);
            result = result.append('-').append(natives.get(osName));
        }
        result = result.append(".jar");
        return libraryPathSubstitutor.substitute(result.toString());
    }

    /**
     *
     * @return True if this library is compatible with the current operating system
     */
    boolean isCompatible() {
        // the following condition is very important and can brackets can be ignored while reading(they're just to increase readability)
        // library is compatible if:
        //    (there are no rules) OR ((action is allow) AND (there are EITHER ((no natives) OR (natives for this platform are available))))
        return rules.allows(Platform.getCurrentPlatform(), System.getProperty("os.version"), FeaturePreds.ALL)
                && (!hasNatives() || natives.containsKey(Platform.getCurrentPlatform().getMinecraftName()) || natives.containsKey(Platform.wrapName(Platform.getCurrentPlatform().getMinecraftName())) );
    }

    /**
     *
     * @return True if there are natives for any platform
     */
    public boolean hasNatives() {
        return !natives.isEmpty();
    }

    /**
     *
     * @return IExtractRules that apply to this library
     */
    public IExtractRules getExtractRules() {
        return extractRules;
    }

    public Artifact getArtifact() {
        return artifact;
    }
}
