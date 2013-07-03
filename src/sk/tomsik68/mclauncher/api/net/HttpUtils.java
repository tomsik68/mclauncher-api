package sk.tomsik68.mclauncher.api.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;
import java.security.cert.Certificate;

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
            response = response.append(line).append('\r');
        }
        connection.disconnect();
        return response.toString();
    }

    public static String securePost(String url, InputStream keyInput, String parameters) throws Exception {
        URL u = new URL(url);
        HttpsURLConnection connection = (HttpsURLConnection) u.openConnection();
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", "" + parameters.getBytes().length);
        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        connection.connect();
        Certificate cert = connection.getServerCertificates()[0];
        byte[] serverKey = cert.getPublicKey().getEncoded();
        DataInputStream dis = new DataInputStream(keyInput);
        for (int i = 0; i < serverKey.length; ++i) {
            if (dis.readByte() != serverKey[i]) {
                throw new SecurityException("Invalid Server Key!");
            }
        }
        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
        dos.writeBytes(URLEncoder.encode(parameters, "UTF-8"));
        dos.flush();
        dos.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = br.readLine()) != null) {
            response = response.append(line).append('\r');
        }
        br.close();
        connection.disconnect();
        return response.toString();
    }

    public static <T extends Object> T getObjectFromJSON(String url, Class<T> objectClass) throws Exception {
        String response = httpGet(url);
        return gson.fromJson(response, objectClass);
    }
}
