package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

public class MCDownloadVersion implements IVersion {
    private final String id, time, releaseTime, type, processArgs, minecraftArgs, mainClass, incompatibilityReason;
    private final int minimumLauncherVersion;

    public MCDownloadVersion(JSONObject json) {
        id = json.get("id").toString();
        time = json.get("time").toString();
        releaseTime = json.get("releaseTime").toString();
        type = json.get("type").toString();
        processArgs = json.get("processArguments").toString();
        minecraftArgs = json.get("minecraftArguments").toString();
        minimumLauncherVersion = Integer.parseInt(json.get("minimumLauncherVersion").toString());
        mainClass = json.get("mainClass").toString();
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

    @Override
    public IVersionInstaller<? extends IVersion> getInstaller() {
        // TODO
        return null;
    }

    @Override
    public IVersionLauncher<? extends IVersion> getLauncher() {
        // TODO
        return null;
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

    public String getIncompatibilityReason() {
        return incompatibilityReason;
    }

}
