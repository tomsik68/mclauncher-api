package sk.tomsik68.mclauncher.util;

import java.io.File;

/**
 * Concatenates multiple strings and appends file separator in between
 */
public class FilePathBuilder {
    private String path;

    public FilePathBuilder(File start) {
        this.path = start.getAbsolutePath();
    }

    public FilePathBuilder append(String s) {
        path += File.separator + s;
        return this;
    }

    public File getResult() {
        return new File(path);
    }
}
