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
    public static final String DEFAULT_LIBS_URL = "https://libraries.minecraft.net/";
    private final StringSubstitutor libraryPathSubstitutor = new StringSubstitutor("${%s}");
    private final String name;
    private final Map<String, String> natives;
    private final RuleList rules;
    private final IExtractRules extractRules;
    private final Artifact artifact;
    private final Map<String, Artifact> classifiers;

    private Library(String name, Map<String, String> natives, RuleList rules, IExtractRules extractRules, Artifact artifact, Map<String, Artifact> classifiers) {
        this.name = name;
        this.natives = Collections.unmodifiableMap(natives);
        this.rules = rules;
        this.extractRules = extractRules;
        this.artifact = artifact;
        this.classifiers = Collections.unmodifiableMap(classifiers);
    }

    static Library fromJson(JSONObject json) {
        String name = json.get("name").toString();
        Map<String, String> natives = new HashMap<>();
        Map<String, Artifact> classifiers = new HashMap<>();
        RuleList rules;
        Artifact artifact = null;
        IExtractRules extractRules;

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
            extractRules = LibraryExtractRules.fromJson((JSONObject) json.get("extract"));
        } else {
            extractRules = null;
        }

        JSONObject downloads = (JSONObject) json.get("downloads");
        if (downloads != null && downloads.containsKey("artifact")) {
            artifact = Artifact.fromJson((JSONObject) downloads.get("artifact"));
        } else {
            if (json.containsKey("url")) {
                artifact = Artifact.fromUrl(json.get("url").toString());
            } else if(downloads == null) {
                String url = DEFAULT_LIBS_URL + nameToPath(name) + ".jar";
                artifact = Artifact.fromUrl(url);
            }
        }


        if (downloads != null && downloads.containsKey("classifiers")) {
            JSONObject cls = (JSONObject) downloads.get("classifiers");
            assert cls != null;
            for (Map.Entry<String, Object> entry : cls.entrySet()) {
                classifiers.put(entry.getKey(), Artifact.fromJson((JSONObject)entry.getValue()));
            }
        }

        return new Library(name, natives, rules, extractRules, artifact, classifiers);
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

    private static String nameToPath(String name) {
        StringBuilder result = new StringBuilder();
        String[] split = name.split(":");

        result = result.append(split[0].replace('.', '/'));// net/sf/jopt-simple
        result = result.append('/').append(split[1]).append('/').append(split[2]).append('/'); // /jopt-simple/4.4/
        result = result.append(split[1]).append('-').append(split[2]); // jopt-simple-4.4

        return result.toString();
    }

    /**
     * Returns relative path of library as string. Relative path is used in URLs, file paths.
     * You can read more about this on wiki...
     * @return Relative path of library.
     */
    public String getPath() {
        libraryPathSubstitutor.setVariable("arch", Platform.getCurrentPlatform().getArchitecture());

        StringBuilder result = new StringBuilder(nameToPath(name));

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
