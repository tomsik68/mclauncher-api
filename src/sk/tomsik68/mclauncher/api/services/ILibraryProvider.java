package sk.tomsik68.mclauncher.api.services;

import java.io.File;

public interface ILibraryProvider extends IService {
    public File[] getDefaultLWJGLJars();
}
