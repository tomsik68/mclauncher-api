package sk.tomsik68.mclauncher.backend;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;

import java.io.File;

public final class MinecraftLauncherBackend {
    private final MinecraftInstance minecraftInstance;

    public MinecraftLauncherBackend(File minecraftDirectory){
        minecraftInstance = new MinecraftInstance(minecraftDirectory);
    }
}
