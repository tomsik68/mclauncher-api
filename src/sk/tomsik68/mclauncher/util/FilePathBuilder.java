package sk.tomsik68.mclauncher.util;

import java.io.File;

public class FilePathBuilder {
    private String path;

    public FilePathBuilder(File start) {
        this.path = start.getAbsolutePath();
    }

    public FilePathBuilder append(String s) {
        path += File.separator + s;
        return this;
    }
    public File getResult(){
        return new File(path);
    }
}
