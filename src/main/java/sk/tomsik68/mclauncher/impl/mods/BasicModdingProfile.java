package sk.tomsik68.mclauncher.impl.mods;

import sk.tomsik68.mclauncher.api.mods.IModdingProfile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class BasicModdingProfile implements IModdingProfile {
    private final ArrayList<File> coreMods = new ArrayList<File>();

    public BasicModdingProfile(){}

    public final void addCoreMod(File jar){
        coreMods.add(jar);
    }

    @Override
    public List<File> getCoreMods() {
        return coreMods;
    }
}
