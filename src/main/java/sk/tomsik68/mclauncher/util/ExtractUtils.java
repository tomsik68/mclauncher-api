package sk.tomsik68.mclauncher.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class ExtractUtils {
    private static final IExtractRules anarchy = new IExtractRules() {
        @Override
        public boolean accepts(ZipEntry entry) {
            return true;
        }
    };

    /**
     * Extracts a ZIP-compatible(even JAR) file to specified directory
     * @param jar The archive to be extracted
     * @param dir Where to extract the archive
     * @throws Exception I/O errors or decompression errors
     */
    public static void extractZipWithoutRules(File jar, File dir) throws Exception {
        extractZipWithRules(jar, dir, anarchy);
    }

    /**
     * Extract a ZIP-compatible(even JAR) file to specified directory respecting the specified rules
     * @param jar The archive to be extracted
     * @param dir Where to extract the archive
     * @param rules Rules to be used during extraction
     * @throws Exception I/O errors or decompression errors
     */
    public static void extractZipWithRules(File jar, File dir, IExtractRules rules) throws Exception {
        if (rules == null)
            rules = anarchy;
        ZipFile zf = new ZipFile(jar);
        Enumeration<? extends ZipEntry> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = (ZipEntry) entries.nextElement();
            if (rules.accepts(zipEntry))
                extractZipEntry(zf, zipEntry, dir);
        }
    }

    private static void extractZipEntry(ZipFile zf, ZipEntry zipEntry, File dir) throws Exception {
        File destFile = new File(dir, zipEntry.getName());
        if (zipEntry.isDirectory())
            destFile.mkdirs();
        else {
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();
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
