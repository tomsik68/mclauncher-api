package sk.tomsik68.mclauncher.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ExtractUtils {
    private static final IExtractRules anarchy = new IExtractRules() {
        @Override
        public boolean accepts(ZipEntry entry) {
            return true;
        }
    };

    public static void extractZipWithoutRules(File jar, File dir) throws Exception {
        extractZipWithRules(jar, dir, anarchy);
    }

    public static void extractZipWithRules(File jar, File dir, IExtractRules rules) throws Exception {
        ZipFile zf = new ZipFile(jar);
        Enumeration<? extends ZipEntry> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            if (rules.accepts(zipEntry))
                extractZipEntry(zf, zipEntry, dir);
        }
    }

    public static void extractZipEntry(ZipFile zf, ZipEntry zipEntry, File dir) throws Exception {
        if (zipEntry.isDirectory())
            new File(dir, zipEntry.getName()).mkdirs();
        else {
            new File(dir, zipEntry.getName()).mkdirs();
            new File(dir, zipEntry.getName()).delete();
            BufferedInputStream bis = new BufferedInputStream(zf.getInputStream(zipEntry));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(dir, zipEntry.getName())));
            long available = bis.available();
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
