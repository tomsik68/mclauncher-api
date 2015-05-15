package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;

import java.io.File;

final class LibraryProvider {
    private final File libraryFolder;

    LibraryProvider(MinecraftInstance mc){
        this.libraryFolder = new File(mc.getLocation(), "libraries");
    }

    File getLibraryFile(Library library){
        String path = library.getPath().replace('/', File.separatorChar);
        return new File(libraryFolder, path);
    }

    boolean isInstalled(Library library){
        return getLibraryFile(library).exists();
    }

    File getLibraryFolder(){ return libraryFolder; }


}
