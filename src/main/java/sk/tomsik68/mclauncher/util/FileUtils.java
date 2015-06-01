package sk.tomsik68.mclauncher.util;

import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public final class FileUtils {
    public static void createFileSafely(File file) throws Exception {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (!file.exists())
            file.createNewFile();
    }

    public static void downloadFileWithProgress(String url, File dest, IProgressMonitor progress) throws Exception {
        // System.out.println("Downloading "+url);
        createFileSafely(dest);
        String md5 = null;
        if (dest.exists()) {
            md5 = getMD5(dest);
        }

        URL u = new URL(url);
        URLConnection connection = u.openConnection();
        if (md5 != null)
            connection.setRequestProperty("If-None-Match", md5);
        connection.connect();

        BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));

        final int len = connection.getContentLength();
        if (progress != null)
            progress.setMax(len);

        int readBytes = 0;
        byte[] block;

        while (readBytes < len) {
            block = new byte[8192];
            int readNow = in.read(block);
            if (readNow > 0)
                out.write(block, 0, readNow);
            if (progress != null)
                progress.setProgress(readBytes);
            readBytes += readNow;
        }
        out.flush();
        out.close();
        in.close();
        if (progress != null)
            progress.finish();
    }

    public static void copyFile(File from, File to) throws Exception {
        createFileSafely(to);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(to));
        byte[] block;
        while (bis.available() > 0) {
            block = new byte[8192];
            final int readNow = bis.read(block);
            bos.write(block, 0, readNow);
        }
        bos.flush();
        bos.close();
        bis.close();
    }

    public static void writeFile(File dest, String str) throws Exception {
        createFileSafely(dest);
        FileWriter fw = new FileWriter(dest);
        fw.write(str);
        fw.flush();
        fw.close();
    }

    // this method is copied from original launcher, as the MD5-ing function
    // needs to be the same
    public static String getMD5(File file) throws Exception {
        DigestInputStream stream = null;
        try {
            stream = new DigestInputStream(new FileInputStream(file), MessageDigest.getInstance("MD5"));
            byte[] buffer = new byte[65536];

            int read = stream.read(buffer);
            while (read >= 1)
                read = stream.read(buffer);
        } catch (Exception ignored) {
            return null;
        } finally {
            stream.close();
        }

        return String.format("%1$032x", new Object[]{new BigInteger(1, stream.getMessageDigest().digest())});
    }
}
