package sk.tomsik68.mclauncher.api.net;

public interface IDownloadListener<T> {
    public void onDownload(T response);
}
