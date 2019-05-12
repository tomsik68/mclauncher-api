package sk.tomsik68.mclauncher.api.mods;


import java.io.File;
import java.util.List;

/**
 * Describes current set of mods that will be injected into JAR file
 */
public interface IModdingProfile {
    /**
     * Returns <code>separator</code>-separated list of absolute paths to JAR files that will be injected before libraries
     * @param separator - String used to separate files
     * @return Null for none.
     */
    public File[] injectBeforeLibs(String separator);

    /**
     * Returns <code>separator</code>-separated list of absolute paths to JAR files that will be injected after libraries
     * @param separator - String used to separate files
     * @return Null for none.
     */
    public File[] injectAfterLibs(String separator);

    /**
     * Checks if this library should be loaded with our mods.
     * @param libraryName - Name of library to test
     * @return True if specified library may be injected along with all vanilla libraries
     */
    public boolean isLibraryAllowed(String libraryName);

    /**
     *
     * @return Custom game JAR file to use. If you don't want to change it, return null
     */
    public File getCustomGameJar();

    /**
     *
     * @return Name of main class to use while launching Minecraft.
     */
    public String getMainClass();

    /**
     * Minecraft arguments are arguments that will be available in minecraft's main method.
     * These contain mostly user information, but also assets path, saves path etc, which might be useful...
     * @param minecraftArguments Array of minecraft arguments created by launcher
     * @return Array of string which is formatted in the same way as the input array. If you don't want to make any changes, return null or <code>minecraftArguments</code>
     */
    public List<String> changeMinecraftArguments(List<String> minecraftArguments);

    /**
     *
     * @return List of parameters that will be appended after all parameters to launch the JAR. These most likely won't influence the launching process, but you may find it useful...
     */
    public List<String> getLastParameters();
}
