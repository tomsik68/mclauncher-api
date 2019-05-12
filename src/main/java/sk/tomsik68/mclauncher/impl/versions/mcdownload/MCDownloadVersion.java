package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;
import sk.tomsik68.mclauncher.impl.common.Platform;

import java.util.ArrayList;
import java.util.List;

final class MCDownloadVersion implements IVersion, IJSONSerializable {
    private static final MCDownloadVersionInstaller installer = new MCDownloadVersionInstaller();
    private static final IVersionLauncher launcher = new MCDownloadVersionLauncher();
    private static final String DEFAULT_ASSETS_INDEX = "legacy";
    private ArgumentList jvmArgs;
    private ArgumentList gameArgs;

    private String id, time, releaseTime, type, mainClass, jarVersion;
    private Integer minimumLauncherVersion;
    private final JSONObject json;
    private String incompatibilityReason, assets, inheritsFrom;
    private RuleList rules;
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

        if (json.containsKey("processArguments")) {
            jvmArgs = ArgumentList.fromString(json.get("processArguments").toString());
        } else if(json.containsKey("arguments")) {
            JSONObject arguments = (JSONObject) json.get("arguments");
            JSONArray jvm = (JSONArray) arguments.get("jvm");
            jvmArgs = ArgumentList.fromArray(jvm);
        } else {
            jvmArgs = ArgumentList.empty();
        }

        if (json.containsKey("minecraftArguments")) {
            gameArgs = ArgumentList.fromString(json.get("minecraftArguments").toString());
        } else if(json.containsKey("arguments")) {
            JSONObject arguments = (JSONObject) json.get("arguments");

            JSONArray game = (JSONArray) arguments.get("game");

            gameArgs = ArgumentList.fromArray(game);
        } else {
            gameArgs = ArgumentList.empty();
        }

        if (json.containsKey("minimumLauncherVersion"))
            minimumLauncherVersion = Integer.parseInt(json.get("minimumLauncherVersion").toString());
        mainClass = json.get("mainClass").toString();
        if (json.containsKey("assets"))
            assets = json.get("assets").toString();
        rules = RuleList.fromJson((JSONArray) json.get("rules"));

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

    ArgumentList getGameArgs() {
        return gameArgs;
    }

    ArgumentList getJvmArgs() {
        return jvmArgs;
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
        return rules.allows(Platform.getCurrentPlatform(), System.getProperty("os.version"), FeaturePreds.ALL);
    }

    @Override
    public JSONObject toJSON() {
        return json;
    }

    String getAssetsIndexName() {
        if (assets == null)
            return DEFAULT_ASSETS_INDEX;
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

        if(gameArgs.isEmpty())
            gameArgs = parent.gameArgs;

        if (jvmArgs.isEmpty())
            jvmArgs = parent.jvmArgs;

        if(minimumLauncherVersion == null)
            minimumLauncherVersion = parent.getMinimumLauncherVersion();

        if(mainClass == null)
            mainClass = parent.getMainClass();

        if(incompatibilityReason == null)
            incompatibilityReason = parent.getIncompatibilityReason();

        if(assets == null)
            assets = parent.getAssetsIndexName();

        libraries.addAll(parent.getLibraries());

        if(jarVersion == null || jarVersion.isEmpty()){
            jarVersion = parent.getJarVersion();
        }

        rules = rules.and(parent.rules);

        needsInheritance = false;
        MCLauncherAPI.log.finer("Inheriting version ".concat(id).concat(" finished."));
    }
}
