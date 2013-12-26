package sk.tomsik68.mclauncher.util;

import java.io.File;

public class FilePathBuilder {
    private File file;

    public FilePathBuilder(File start) {
        this.file = start;
    }

    public FilePathBuilder append(String s) {
        file = new File(file, s);
        return this;
    }
    public File getResult(){
        return file;
    }
}
