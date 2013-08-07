package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

public class MCDownloadVersion implements IVersion, IJSONSerializable {
    private static final MCDownloadVersionInstaller installer = new MCDownloadVersionInstaller();

    private final String id, time, releaseTime, type, minecraftArgs, mainClass;
    private String incompatibilityReason, processArgs;
    private final int minimumLauncherVersion;
    private ArrayList<Rule> rules = new ArrayList<Rule>();
    private ArrayList<Library> libraries = new ArrayList<Library>();
    private final JSONObject json;
    private static final IVersionLauncher launcher = null;

    public MCDownloadVersion(JSONObject json) {
        this.json = json;
        id = json.get("id").toString();
        time = json.get("time").toString();
        releaseTime = json.get("releaseTime").toString();
        type = json.get("type").toString();
        if (json.containsKey("processArguments"))
            processArgs = json.get("processArguments").toString();
        minecraftArgs = json.get("minecraftArguments").toString();
        minimumLauncherVersion = Integer.parseInt(json.get("minimumLauncherVersion").toString());
        mainClass = json.get("mainClass").toString();
        if (json.containsKey("rules")) {
            JSONArray rulesArray = (JSONArray) json.get("rules");
            for (Object o : rulesArray) {
                JSONObject jsonRule = (JSONObject) o;
                rules.add(new Rule(jsonRule));
            }
        }
        if (json.containsKey("libraries")) {
            JSONArray libs = (JSONArray) json.get("libraries");
            for (int i = 0; i < libs.size(); ++i) {
                libraries.add(new Library((JSONObject) libs.get(i)));
            }
        }
        if (json.containsKey("incompatibilityReason"))
            incompatibilityReason = json.get("incompatibilityReason").toString();
    }

    @Override
    public int compareTo(IVersion arg0) {
        return getId().compareTo(arg0.getId());
    }

    @Override
    public String getDisplayName() {
        return type.concat(" ").concat(id);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUniqueID() {
        return type.charAt(0) + getId();
    }

    public String getTime() {
        return time;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public String getType() {
        return type;
    }

    public String getProcessArgs() {
        return processArgs;
    }

    public String getMinecraftArgs() {
        return minecraftArgs;
    }

    public int getMinimumLauncherVersion() {
        return minimumLauncherVersion;
    }

    public String getMainClass() {
        return mainClass;
    }

    @Override
    public String getIncompatibilityReason() {
        return incompatibilityReason;
    }

    @Override
    public IVersionInstaller getInstaller() {
        return installer;
    }

    @Override
    public IVersionLauncher getLauncher() {
        return launcher;
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    @Override
    public boolean isCompatible() {
        boolean compat = true;
        for (Rule rule : rules) {
            compat = compat && rule.allows();
        }
        return compat;
    }

    @Override
    public JSONObject toJSON() {
        return json;
    }

}
