package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.impl.common.Platform;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

final class Rule {
    private final Action action;
    private final IOperatingSystem restrictedOs;
    private final String restrictedOsVersionPattern;
    private final String architecture;
    private final Map<String, Boolean> features;

    private Rule(Action action, IOperatingSystem restrictedOs, String restrictedOsVersionPattern, String architecture, Map<String, Boolean> features) {
        this.action = action;
        this.restrictedOs = restrictedOs;
        this.restrictedOsVersionPattern = restrictedOsVersionPattern;
        this.architecture = architecture;
        this.features = Collections.unmodifiableMap(features);
    }

    static Rule fromJson(JSONObject json) {
        Action action;
        IOperatingSystem restrictedOs;
        String restrictedOsVersionPattern;
        String architecture;
        Map<String, Boolean> features;
        action = Action.valueOf(json.get("action").toString().toUpperCase());
        if (json.containsKey("os")) {
            JSONObject os = (JSONObject) json.get("os");
            if (os.containsKey("name"))
                restrictedOs = Platform.osByName(os.get("name").toString());
            else
                restrictedOs = null;
            if (os.containsKey("arch"))
                architecture = os.get("arch").toString().toUpperCase();
            else
                architecture = null;
            if (os.containsKey("version"))
                restrictedOsVersionPattern = os.get("version").toString();
            else
                restrictedOsVersionPattern = null;
        } else {
            restrictedOs = null;
            restrictedOsVersionPattern = null;
            architecture = null;
        }
        if (json.containsKey("features")) {
            JSONObject featuresMap = (JSONObject) json.get("features");
            Map<String, Boolean> f = new HashMap<>();
            for (Map.Entry<String, Object> entry : featuresMap.entrySet()) {
                f.put(entry.getKey(), Boolean.valueOf(entry.getValue().toString()));
            }
            features = Collections.unmodifiableMap(f);
        } else {
            features = Collections.emptyMap();
        }

        return new Rule(action,restrictedOs,restrictedOsVersionPattern, architecture,features);
    }

    public Action getAction() {
        return action;
    }

    public IOperatingSystem getRestrictedOs() {
        return restrictedOs;
    }

    public String getRestrictedOsVersionPattern() {
        return restrictedOsVersionPattern;
    }

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Rule:{");
        sb.append("Action:").append(action).append(',');
        if (restrictedOs != null)
            sb.append("OS:").append(restrictedOs.getDisplayName()).append(',');
        if (restrictedOsVersionPattern != null)
            sb.append("version:").append(restrictedOsVersionPattern);
        sb.append('}');
        return sb.toString();
    }

    /**
     *
     * @return True if this rule is effective, false otherwise
     */
    public boolean applies(IFeaturePredicate pred) {
        // determine if features are satisfied
        boolean satisfied = true;
        for (Map.Entry<String, Boolean> feature : features.entrySet()) {
            satisfied &= pred.isFeatureSatisfied(feature.getKey(), feature.getValue());
        }
        if (!satisfied)
            return false;

        // if there's no OS specified, it applies to all OSs
        if (getRestrictedOs() == null) {
            return architecture == null || System.getProperty("os.arch").equalsIgnoreCase(architecture);
        } else {
            // if our OS is the restricted OS
            if (getRestrictedOs() == Platform.getCurrentPlatform()) {
                // see if there's a version specified
                if (restrictedOsVersionPattern == null) {
                    // if there's no version, it applies to all versions
                    return true;
                } else {
                    // if there's a version specified, compile it to a pattern and try to match it against system property "os.version"
                    boolean result = Pattern.matches(restrictedOsVersionPattern, System.getProperty("os.version"));
                    return result;
                }
            } else {
                // our OS is not restricted by this rule
                return false;
            }
        }
    }

    /**
     * All possible actions for rules
     */
    // they're pretty self-explanatory. There's really nothing more to write about it.
    // maybe just the fact that Action is not the only thing that decides whether a Rule is effective
    public enum Action {
        ALLOW,
        DISALLOW
    }
}
