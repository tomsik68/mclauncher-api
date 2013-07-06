package sk.tomsik68.mclauncher.impl.versions.mcassets;

import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

public class MCAssetsVersion implements IVersion {
    private final String id;
    private static final MCAssetsVersionInstaller installer = new MCAssetsVersionInstaller();
    public MCAssetsVersion(String vid) {
        id = vid;
    }

    @Override
    public int compareTo(IVersion o) {
        return getId().compareTo(o.getId());
    }

    @Override
    public String getDisplayName() {
        return getId();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUniqueID() {
        return getId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public IVersionInstaller<? extends IVersion> getInstaller() {
        return installer;
    }

    @Override
    public <V extends IVersion> IVersionLauncher<V> getLauncher() {
        return null;
    }

}
