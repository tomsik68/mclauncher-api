package sk.tomsik68.mclauncher.api.mods;

import java.io.File;
import java.util.List;

/**
 * Represents a list of mods injected to the JAR file
 */
public interface IModdingProfile {
    /**
     *
     * @return {@link List} of {@link File}s which denote JAR files to be loaded
     */
    public List<File> getCoreMods();
}
