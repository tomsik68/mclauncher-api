package sk.tomsik68.mclauncher.api.versions;

public interface IVersion extends Comparable<IVersion> {
    public String getDisplayName();

    public String getId();

    public String getUniqueID();

    public IVersionInstaller<? extends IVersion> getInstaller();

    public IVersionLauncher<? extends IVersion> getLauncher();
}
