package sk.tomsik68.mclauncher.api.net;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    public String httpGet(String url) throws Exception{
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        return url;
    }
}
