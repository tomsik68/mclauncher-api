package sk.tomsik68.mclauncher.impl.common.mc;

import java.io.File;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.services.IJarProvider;
import sk.tomsik68.mclauncher.api.services.ILibraryProvider;

public class MinecraftInstance implements IMinecraftInstance {
    private final IJarProvider jars;
    private final ILibraryProvider libs;
    private final File dir, assets;

    public MinecraftInstance(File dir) {
        this.dir = dir;
        if (!dir.exists())
            dir.mkdirs();
        jars = new JarProvider(this.dir);
        libs = new LibraryProvider(this);
        assets = new File(dir, "assets");
    }

    @Override
    public File getLocation() {
        return dir;
    }

    @Override
    public IJarProvider getJarProvider() {
        return jars;
    }

    @Override
    public ILibraryProvider getLibraryProvider() {
        return libs;
    }

    @Override
    public File getAssetsDirectory() {
        return assets;
    }

}
