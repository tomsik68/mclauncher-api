package sk.tomsik68.mclauncher.api.versions;

/**
 * Simple interface for listener which will be notified when new version is installed
 *
 * @author Tomsik68
 */
public interface IVersionInstallListener {
    /**
     * This method will be called when the version is installed
     *
     * @param version Newly installed version
     */
    public void versionInstalled(IVersion version);
}
