package sk.tomsik68.mclauncher.util;

import java.util.zip.ZipEntry;

public interface IExtractRules {
    public boolean accepts(ZipEntry entry);
}
