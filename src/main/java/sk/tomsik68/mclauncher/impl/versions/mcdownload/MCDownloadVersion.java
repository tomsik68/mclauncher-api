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
    private Artifact client, server;
    private Artifact assetIndex;
    private String assetsIndexName;
    private Integer minimumLauncherVersion;
    private final JSONObject json;
    private String incompatibilityReason, inheritsFrom;
    private RuleList rules;
    private List<Library> libraries = new ArrayList<Library>();

    private boolean needsInheritance;

    private MCDownloadVersion(String id, String time, String releaseTime, String type, String mainClass, String jarVersion, Artifact client, Artifact server, Artifact assetIndex, String assetsIndexName, Integer minimumLauncherVersion, JSONObject json, String incompatibilityReason, String inheritsFrom, RuleList rules, List<Library> libraries) {
        this.id = id;
        this.time = time;
        this.releaseTime = releaseTime;
        this.type = type;
        this.mainClass = mainClass;
        this.jarVersion = jarVersion;
        this.client = client;
        this.server = server;
        this.assetIndex = assetIndex;
        this.assetsIndexName = assetsIndexName;
        this.minimumLauncherVersion = minimumLauncherVersion;
        this.json = json;
        this.incompatibilityReason = incompatibilityReason;
        this.inheritsFrom = inheritsFrom;
        this.rules = rules;
        this.libraries = libraries;
    }

    static MCDownloadVersion fromJson(JSONObject json) {
        ArgumentList jvmArgs;
        ArgumentList gameArgs;
        String id, time, releaseTime, type, mainClass, jarVersion;
        Artifact client = null, server = null;
        Artifact assetIndex;
        String assetsIndexName;
        Integer minimumLauncherVersion = null;
        String incompatibilityReason = "", inheritsFrom = null;
        RuleList rules;
        List<Library> libraries = new ArrayList<Library>();
        boolean needsInheritance;

        id = json.get("id").toString();
        if(json.containsKey("jar")) {
            jarVersion = json.get("jar").toString();
        } else {
            jarVersion = id;
        }
        time = json.get("time").toString();
        releaseTime = json.get("releaseTime").toString();
        type = json.get("type").toString();
        assetsIndexName = json.get("assets").toString();

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
        assetIndex = Artifact.fromJson((JSONObject) json.get("assetIndex"));
        rules = RuleList.fromJson((JSONArray) json.get("rules"));

        if (json.containsKey("libraries")) {
            JSONArray libs = (JSONArray) json.get("libraries");
            for (int i = 0; i < libs.size(); ++i) {
                libraries.add(Library.fromJson((JSONObject) libs.get(i)));
            }
        }

        if (json.containsKey("downloads")) {
            JSONObject downloads = (JSONObject) json.get("downloads");
            client = Artifact.fromJson((JSONObject) downloads.get("client"));
            server = Artifact.fromJson((JSONObject) downloads.get("server"));
        }

        if (json.containsKey("incompatibilityReason"))
            incompatibilityReason = json.get("incompatibilityReason").toString();
        if (json.containsKey("inheritsFrom")) {
            inheritsFrom = json.get("inheritsFrom").toString();
            needsInheritance = true;
        } else
            needsInheritance = false;
        return new MCDownloadVersion(id,time,releaseTime,type,mainClass,jarVersion,client,server,assetIndex,assetsIndexName,minimumLauncherVersion,json,incompatibilityReason,inheritsFrom,rules,libraries);
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

        if (assetsIndexName == null)
            assetsIndexName = parent.getAssetsIndexName();

        if(assetIndex == null)
            assetIndex = parent.getAssetIndex();

        libraries.addAll(parent.getLibraries());

        if(jarVersion == null || jarVersion.isEmpty()){
            jarVersion = parent.getJarVersion();
        }

        rules = rules.and(parent.rules);

        needsInheritance = false;
        MCLauncherAPI.log.finer("Inheriting version ".concat(id).concat(" finished."));
    }

    Artifact getAssetIndex() {
        return assetIndex;
    }

    public Artifact getClient() {
        return client;
    }

    public Artifact getServer() {
        return server;
    }

    public String getAssetsIndexName() {
        return assetsIndexName;
    }
}
