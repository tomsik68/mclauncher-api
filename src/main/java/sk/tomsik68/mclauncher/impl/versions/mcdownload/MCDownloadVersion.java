package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.Rule.Action;

import java.util.ArrayList;
import java.util.List;

final class MCDownloadVersion implements IVersion, IJSONSerializable {
    private static final MCDownloadVersionInstaller installer = new MCDownloadVersionInstaller();
    private static final IVersionLauncher launcher = new MCDownloadVersionLauncher();
    private static final String DEFAULT_ASSETS_INDEX = "legacy";

    private String id, time, releaseTime, type, minecraftArgs, mainClass, jarVersion;
    private int minimumLauncherVersion;
    private final JSONObject json;
    private String incompatibilityReason, processArgs, assets, inheritsFrom;
    private ArrayList<Rule> rules = new ArrayList<Rule>();
    private ArrayList<Library> libraries = new ArrayList<Library>();

    private boolean needsInheritance;

    MCDownloadVersion(JSONObject json) {
        this.json = json;
        id = json.get("id").toString();
        if(json.containsKey("jar")) {
            jarVersion = json.get("jar").toString();
        } else {
            jarVersion = id;
        }
        time = json.get("time").toString();
        releaseTime = json.get("releaseTime").toString();
        type = json.get("type").toString();
        if (json.containsKey("processArguments"))
            processArgs = json.get("processArguments").toString();
        minecraftArgs = json.get("minecraftArguments").toString();
        minimumLauncherVersion = Integer.parseInt(json.get("minimumLauncherVersion").toString());
        mainClass = json.get("mainClass").toString();
        if (json.containsKey("assets"))
            assets = json.get("assets").toString();
        else
            assets = DEFAULT_ASSETS_INDEX;
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
        if (json.containsKey("inheritsFrom")) {
            inheritsFrom = json.get("inheritsFrom").toString();
            needsInheritance = true;
        } else
            needsInheritance = false;
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

    String getTime() {
        return time;
    }

    String getReleaseTime() {
        return releaseTime;
    }

    String getType() {
        return type;
    }

    String getProcessArgs() {
        return processArgs;
    }

    String getMinecraftArgs() {
        return minecraftArgs;
    }

    int getMinimumLauncherVersion() {
        return minimumLauncherVersion;
    }

    String getMainClass() {
        return mainClass;
    }

    String getInheritsFrom(){ return inheritsFrom; }

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

    List<Library> getLibraries() {
        return libraries;
    }

    /**
     *
     * @return True if this version is compatible with our current operating system
     */
    public boolean isCompatible() {
        Action action = null;
        for (Rule rule : rules) {
            if (rule.applies())
                action = rule.getAction();
        }
        return rules.isEmpty() || action == Action.ALLOW;
    }

    @Override
    public JSONObject toJSON() {
        return json;
    }

    String getAssetsIndexName() {
        return assets;
    }

    boolean needsInheritance(){ return needsInheritance; }

    String getJarVersion(){
        return jarVersion;
    }

    void doInherit(MCDownloadVersion parent) {
        MCLauncherAPI.log.finer("Inheriting version ".concat(id).concat(" from ").concat(parent.getId()));
        if(!parent.getId().equals(getInheritsFrom())){
            throw new IllegalArgumentException("Wrong inheritance version passed!");
        }

        if(minecraftArgs == null)
            minecraftArgs = parent.getMinecraftArgs();

        if(mainClass == null)
            mainClass = parent.getMainClass();

        if(incompatibilityReason == null)
            incompatibilityReason = parent.getIncompatibilityReason();

        if(assets == null)
            assets = parent.getAssetsIndexName();

        libraries.addAll(parent.getLibraries());
        rules.addAll(parent.rules);


        if(jarVersion == null || jarVersion.isEmpty()){
            jarVersion = parent.getJarVersion();
        }

        if(rules.isEmpty())
            rules.addAll(parent.rules);

        needsInheritance = false;
        MCLauncherAPI.log.finer("Inheriting version ".concat(id).concat(" finished."));
    }
}
