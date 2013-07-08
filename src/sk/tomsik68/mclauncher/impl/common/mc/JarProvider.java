package sk.tomsik68.mclauncher.impl.common.mc;

import java.io.File;

import sk.tomsik68.mclauncher.api.services.IJarProvider;
import sk.tomsik68.mclauncher.api.versions.IVersion;

public class JarProvider implements IJarProvider {
    private final File binFolder;

    public JarProvider(File mc) {
        binFolder = new File(mc, "bin");
    }

    @Override
    public String getBranding() {
        return "custom";
    }

    @Override
    public File getVersionFile(String uniqueID) {
        return new File(binFolder, uniqueID.concat(File.separator).concat("minecraft.jar"));
    }

    @Override
    public void prepareVersionInstallation(IVersion version) {
        if (!new File(binFolder, version.getUniqueID()).exists())
            new File(binFolder, version.getUniqueID()).mkdirs();
    }

    @Override
    public File getBinFolder() {
        return binFolder;
    }

}
