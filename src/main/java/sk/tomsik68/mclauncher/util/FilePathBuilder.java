package sk.tomsik68.mclauncher.util;

import java.io.File;

/**
 * Concatenates multiple strings and appends file separator in between
 */
public final class FilePathBuilder {
    private StringBuilder pathBuilder;

    public FilePathBuilder(File start) {
        this.pathBuilder = new StringBuilder(start.getAbsolutePath());
    }

    public FilePathBuilder append(String s) {
        pathBuilder = pathBuilder.append(File.separator).append(s);
        return this;
    }

    public File getResult() {
        return new File(pathBuilder.toString());
    }
}
