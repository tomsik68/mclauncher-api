package sk.tomsik68.mclauncher.util;

import net.minidev.json.JSONStyle;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.Certificate;

/**
 * Http communication utilities
 */
public final class HttpUtils {

    /**
     * Executes a simple HTTP-GET request
     * @param url URL to request
     * @return The result of request
     * @throws Exception I/O Exception or HTTP errors
     */
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

    /**
     * Execute a secured POST request
     * @param url URL to request
     * @param keyInput the secret key to be used
     * @param parameters Parameters in form <code>name=Tom&amp;password=pass123</code>. They needn't to be URL-encoded(it will be done automatically)
     * @return The result of request
     * @throws Exception I/O Exception, HTTP errors or invalid key
     */
    public static String securePostWithKey(String url, InputStream keyInput, String parameters) throws Exception {
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
        dos.writeBytes(URLEncoder.encode(parameters, "utf-8"));
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

    /**
     * Posts a JSON object to URL and returns result
     * @param url URL to request
     * @param request the object to send using post. It will be serialized to JSON string
     * @return Result of request
     * @throws Exception I/O Exception or HTTP errors
     */
    public static String doJSONPost(String url, IJSONSerializable request) throws Exception {
        URL u = new URL(url);
        String json = request.toJSON().toJSONString(JSONStyle.LT_COMPRESS);
        byte[] bytes = json.getBytes();

        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setRequestProperty("Content-Length", "" + bytes.length);
        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
        dos.write(bytes);
        dos.flush();
        dos.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result = result.append(line).append('\r');
        }
        br.close();
        connection.disconnect();
        return result.toString();
    }

}
