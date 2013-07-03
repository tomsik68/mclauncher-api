package sk.tomsik68.mclauncher.api.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

public class HttpUtils {
    private static final Gson gson = new Gson();

    public static String httpGet(String url) throws Exception {
        URL u = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        InputStream is = connection.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null) {
            response = response.append(line).append('\n');
        }
        return response.toString();
    }

    public static <T extends Object> T getObjectFromJSON(String url, Class<T> objectClass) throws Exception {
        String response = httpGet(url);
        return gson.fromJson(response, objectClass);
    }
}
