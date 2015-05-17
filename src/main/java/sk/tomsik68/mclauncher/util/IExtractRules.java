package sk.tomsik68.mclauncher.util;

import java.util.zip.ZipEntry;

/**
 * ZIP Extraction rules
 */
public interface IExtractRules {
    /**
     * Decides whether or not a ZipEntry should be extracted
     * @param entry ZipEntry that is being decided about
     * @return True if this entry should be extracted, false otherwise
     */
    public boolean accepts(ZipEntry entry);
}
