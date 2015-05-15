package sk.tomsik68.mclauncher.api.common.mc;

import java.io.File;

/**
 * Class that holds information about Minecraft Folder
 *
 * @author Tomsik68
 */
public class MinecraftInstance {
    private final File dir;

    public MinecraftInstance(File dir){
        this.dir = dir;
    }

    /**
     * @return Working directory
     */
    public final File getLocation(){
        return dir;
    }
}
