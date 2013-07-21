package sk.tomsik68.mclauncher.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import net.minidev.json.JSONStyle;

import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import java.security.cert.Certificate;

public class HttpUtils {

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
        dos.writeBytes(parameters);
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

    public static String doJSONPost(String url, IJSONSerializable request) throws Exception {
        URL u = new URL(url);
        String json = request.toJSON().toJSONString(JSONStyle.LT_COMPRESS);
        byte[] bytes = json.getBytes();

        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

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
