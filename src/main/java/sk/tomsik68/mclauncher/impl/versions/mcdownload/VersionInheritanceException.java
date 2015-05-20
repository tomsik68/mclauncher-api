package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import sk.tomsik68.mclauncher.api.versions.IVersion;

public final class VersionInheritanceException extends Exception {
    private final IVersion version;

    public VersionInheritanceException(IVersion v) {
        super("This version needs to get inheritance resolved!");
        this.version = v;
    }

    public IVersion getVersion(){ return version; }

}
