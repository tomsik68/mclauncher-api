package sk.tomsik68.mclauncher.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractUtils {

    public static void extractZipWithoutRules(File jar, File dir) throws Exception {
        ZipFile zf = new ZipFile(jar);
        Enumeration<? extends ZipEntry> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            if (zipEntry.isDirectory()) {
                new File(dir, zipEntry.getName()).mkdirs();
            } else {
                new File(dir, zipEntry.getName()).mkdirs();
                new File(dir, zipEntry.getName()).delete();
                BufferedInputStream bis = new BufferedInputStream(zf.getInputStream(zipEntry));
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(dir, zipEntry.getName())));
                long available = zipEntry.getCompressedSize();
                long red = 0;
                byte[] block;
                while (red < available) {
                    block = new byte[8192];
                    int readNow = bis.read(block);
                    bos.write(block, 0, readNow);
                    red += readNow;
                }
                bis.close();
                bos.flush();
                bos.close();
            }
        }
    }

}
