package sk.tomsik68.mclauncher.impl.common.mc;

import sk.tomsik68.mclauncher.api.services.IJarProvider;
import sk.tomsik68.mclauncher.api.versions.IVersion;

import java.io.File;

final class VanillaJarProvider implements IJarProvider {
    private final File versionsFolder;

    public VanillaJarProvider(File mc) {
        versionsFolder = new File(mc, "versions");
    }

    @Override
    public File getVersionFile(IVersion version) {
        return new File(versionsFolder, version.getId() + File.separator + version.getId() + ".jar");
    }

    @Override
    public void prepareVersionInstallation(IVersion version) {
        File versionFolder = new File(versionsFolder, version.getId());
        if (!versionFolder.exists())
            versionFolder.mkdirs();
    }

}
