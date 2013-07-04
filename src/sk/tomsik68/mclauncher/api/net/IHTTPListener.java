package sk.tomsik68.mclauncher.api.net;

public interface IHTTPListener extends IDownloadListener<String> {
    public void onError(Exception e, int httpError);
}
