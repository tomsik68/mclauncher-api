package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.util.regex.Pattern;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.IOperatingSystem;
import sk.tomsik68.mclauncher.impl.common.Platform;

public class Rule {
    private final Action action;
    private IOperatingSystem restrictedOs;
    private String restrictedOsVersionPattern;

    public Rule(JSONObject json) {
        action = Action.valueOf(json.get("action").toString().toUpperCase());
        if (json.containsKey("os")) {
            JSONObject os = (JSONObject) json.get("os");
            restrictedOs = Platform.osByName(os.get("name").toString());
            if (json.containsKey("version"))
                restrictedOsVersionPattern = os.get("version").toString();
        }
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

    public static enum Action {
        ALLOW, DISALLOW
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Rule:{");
        sb.append("Action:").append(action).append(',');
        if (restrictedOs != null)
            sb.append("OS:").append(restrictedOs).append(',');
        if (restrictedOsVersionPattern != null)
            sb.append("version:").append(restrictedOsVersionPattern);
        return sb.toString();
    }

    public boolean applies() {

        if (getRestrictedOs() == null) {
            return true;
        } else {
            if (getRestrictedOs().isCurrent()) {
                if (restrictedOsVersionPattern == null) {
                    return true;
                } else {
                    boolean result = Pattern.matches(restrictedOsVersionPattern, System.getProperty("os.version"));
                    System.out.println(result);
                    return result;
                }
            } else {
                return false;
            }
        }
    }
}
