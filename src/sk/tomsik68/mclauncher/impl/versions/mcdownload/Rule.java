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

    public boolean allows() {
        if (restrictedOs == null || restrictedOsVersionPattern == null) {
            return action == Action.ALLOW;
        }
        if (getRestrictedOs().isCurrent() && !Pattern.matches(restrictedOsVersionPattern, System.getProperty("os.version"))) {
            return action == Action.ALLOW;
        } else
            return true;
    }

    public static enum Action {
        ALLOW, DISALLOW
    }

}
