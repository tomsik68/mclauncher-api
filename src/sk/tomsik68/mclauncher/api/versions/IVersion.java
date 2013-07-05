package sk.tomsik68.mclauncher.api.versions;

public interface IVersion extends Comparable<IVersion> {
    public String getDisplayName();

    public String getId();
    
    public <V extends IVersion> IVersionInstaller<V> getInstaller();
    
    public <V extends IVersion> IVersionLauncher<V> getLauncher();
}
