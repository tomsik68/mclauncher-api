package sk.tomsik68.mclauncher.api.versions;

public final class LatestVersionInformation {
    private final String latestRelease;
    private final String latestSnapshot;
    public LatestVersionInformation(String lastRelease, String lastSnapshot){
        this.latestRelease = lastRelease;
        this.latestSnapshot = lastSnapshot;
    }

    public String getLatestRelease() {
        return latestRelease;
    }

    public String getLatestSnapshot() {
        return latestSnapshot;
    }
}
