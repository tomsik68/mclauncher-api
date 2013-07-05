package sk.tomsik68.mclauncher.api.versions;

public interface IVersionInstaller<V extends IVersion> {
    public void install(V version);
}
