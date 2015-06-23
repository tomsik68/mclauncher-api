package sk.tomsik68.mclauncher.api.versions;

/**
 * Contains information about latest versions in a given version list
 */
public final class LatestVersionInformation {
    private final String latestRelease;
    private final String latestSnapshot;
    public LatestVersionInformation(String lastRelease, String lastSnapshot){
        this.latestRelease = lastRelease;
        this.latestSnapshot = lastSnapshot;
    }

    /**
     *
     * @return ID of latest release version. E.g. "1.8"
     */
    public String getLatestRelease() {
        return latestRelease;
    }

    /**
     *
     * @return ID of latest snapshot. E.g. "14w07a"
     */
    public String getLatestSnapshot() {
        return latestSnapshot;
    }
}
