package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;

import java.util.Objects;

final class Artifact {
    private final String url;
    private final String sha1;
    private final long size;
    private final Long totalSize;

    private Artifact(String url, String sha1, long size, Long totalSize) {
        this.url = url;
        this.sha1 = sha1;
        this.size = size;
        this.totalSize = totalSize;
    }

    static Artifact fromJson(JSONObject json) {
        Objects.requireNonNull(json);
        Long totalSize = null;
        if (json.containsKey("totalSize"))
            Long.parseLong(json.get("totalSize").toString());
        return new Artifact(json.get("url").toString(), json.get("sha1").toString(), Long.parseLong(json.get("size").toString()), totalSize);
    }

    static Artifact fromUrl(String url) {
        Objects.requireNonNull(url);

        return new Artifact(url, "", 0, 0l);
    }

    public String getUrl() {
        return url;
    }

    public String getSha1() {
        return sha1;
    }

    public long getSize() {
        return size;
    }

    public long getTotalSize() {
        return totalSize;
    }
}
