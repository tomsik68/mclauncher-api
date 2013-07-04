package sk.tomsik68.mclauncher.api.net;

public interface IFileDownloadListener extends IDownloadListener<byte[]> {
    public void onError(Exception e);
}
