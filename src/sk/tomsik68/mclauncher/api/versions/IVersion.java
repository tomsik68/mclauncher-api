package sk.tomsik68.mclauncher.api.versions;

public interface IVersion extends Comparable<IVersion> {
    public String getDisplayName();

    public String getId();

    public String getUniqueID();

    public IVersionInstaller getInstaller();

    public IVersionLauncher getLauncher();

    /**
     * 
     * @return if this version is compatible with current runtime.
     */
    public boolean isCompatible();

    public String getIncompatibilityReason();
}
