package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.mods.IModdingProfile;
import sk.tomsik68.mclauncher.api.servers.ServerInfo;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.StringSubstitutor;
import sk.tomsik68.mclauncher.api.login.ISession.Prop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class MCDownloadVersionLauncher implements IVersionLauncher {

    private List<String> getArguments(ArgumentList args, MinecraftInstance mc, File assetsDir,
                                          ISession session, ILaunchSettings settings,
                                          MCDownloadVersion version) {
        // TODO tooo lazy to finish options
        StringSubstitutor subst = new StringSubstitutor("${%s}");
        subst.setVariable("auth_session", session.getSessionID());
        subst.setVariable("auth_access_token", session.getSessionID());
        subst.setVariable("auth_player_name", session.getUsername());
        subst.setVariable("auth_uuid", session.getUUID());
        subst.setVariable("version_name", version.getId());
        subst.setVariable("game_directory", mc.getLocation().getAbsolutePath());
        subst.setVariable("game_assets", assetsDir
                .getAbsolutePath());
        subst.setVariable("assets_root", assetsDir
                .getAbsolutePath());
        subst.setVariable("assets_index_name", version.getAssetsIndexName());
        subst.setVariable("user_type", session.getType().toString()
                .toLowerCase());
        if (session.getProperties() != null
                && !session.getProperties().isEmpty()) {
            JSONObject propertiesObj = new JSONObject();
            List<Prop> properties = session.getProperties();
            for (Prop p : properties) {
                propertiesObj.put(p.name, p.value);
            }
            subst.setVariable("user_properties",
                    propertiesObj.toJSONString(JSONStyle.NO_COMPRESS));
        } else
            subst.setVariable("user_properties", "{}");

        List<String> result = new ArrayList<>();
        for (Argument arg : args) {
            if (arg.applies(Platform.getCurrentPlatform(), System.getProperty("os.version"), FeaturePreds.NONE)) {
                for (String val : arg.getValue()) {
                    result.add(subst.substitute(val));
                }
            }
        }

        return result;
    }

    @Override
    public List<String> getLaunchCommand(ISession session,
                                         MinecraftInstance mc, ServerInfo server, IVersion v,
                                         ILaunchSettings settings, IModdingProfile mods) throws Exception {
        boolean moddingProfileSpecified = (mods != null);
        MCDResourcesInstaller resourcesInstaller = new MCDResourcesInstaller(mc);
        MCDJarManager jarManager = new MCDJarManager(mc);
        LibraryProvider libraryProvider = new LibraryProvider(mc);
        // get local JSON information about the version
        File jsonFile = jarManager.getInfoFile(v);
        MCLauncherAPI.log.fine("Looking for ".concat(jsonFile.getAbsolutePath()));
        if (!jsonFile.exists()) {
            throw new FileNotFoundException(
                    "You need to download the version at first! (JSON description file not found!)");
        }
        // create MCDownloadVersion based on this version
        MCDownloadVersion version = (MCDownloadVersion) v;
        File jarFile = jarManager.getVersionJAR(version);
        // check if the version is compatible with our OS
        MCLauncherAPI.log.fine("Checking version compatibility...");
        if (!version.isCompatible()) {
            throw new VersionIncompatibleException(version);
        }
        MCLauncherAPI.log.fine("Checking version inheritance...");
        // check if everything's inherited
        if(version.needsInheritance())
            throw new VersionInheritanceException(version);
        MCLauncherAPI.log.fine("Checking minecraft launcher API version...");
        // check if we can launch it using the current version of MCLauncherAPI
        if (version.getMinimumLauncherVersion() > MCLauncherAPI.MC_LAUNCHER_VERSION) {
            throw new RuntimeException(
                    "You need to update MCLauncher-API to run this minecraft version! Required API version: "
                            + version.getMinimumLauncherVersion());
        }
        MCLauncherAPI.log.fine("Building the launch command...");
        // build the huge command!
        ArrayList<String> command = new ArrayList<String>();
        // prefix
        if (settings.getCommandPrefix() != null)
            command.addAll(settings.getCommandPrefix());
        // java location if entered or just "java"
        if (settings.getJavaLocation() != null)
            command.add(settings.getJavaLocation().getAbsolutePath());
        else
            command.add("java");
        // -Xms
        if (settings.getInitHeap() != null
                && settings.getInitHeap().length() > 0)
            command.add("-Xms".concat(settings.getInitHeap()));
        // -Xmx
        if (settings.getHeap() != null && settings.getHeap().length() > 0)
            command.add("-Xmx".concat(settings.getHeap()));
        // all additional java arguments
        if (settings.getJavaArguments() != null) {
            command.addAll(settings.getJavaArguments());
        }
        // find natives for this MC version
        final File nativesDir = new File(jarManager.getVersionFolder(v), "natives");
        command.add("-Djava.library.path=" + nativesDir.getAbsolutePath());
        // build classpath
        command.add("-cp");
        StringBuilder librariesString = new StringBuilder();
        final String LIBRARY_SEPARATOR = System.getProperty("path.separator");
        //// mods can inject JARs before libraries
        if(moddingProfileSpecified) {
            File[] customFiles = mods.injectBeforeLibs(LIBRARY_SEPARATOR);
            if(customFiles != null) {
                MCLauncherAPI.log.fine("Injecting custom libraries before library list");
                for (File file : customFiles) {
                    librariesString.append(file.getAbsolutePath()).append(LIBRARY_SEPARATOR);
                }
            }
        }
        MCLauncherAPI.log.fine("Adding library files");
        //// now add library files
        for (Library lib : version.getLibraries()) {
            // each library has to be compatible, installed and allowed by modding profile
            if (lib.getArtifact() != null && lib.isCompatible() && (!moddingProfileSpecified || mods.isLibraryAllowed(lib.getName()))) {
                if (!libraryProvider.isInstalled(lib)) {
                    throw new FileNotFoundException("Library file wasn't found: " + lib.getPath());
                }
                MCLauncherAPI.log.finest("Adding ".concat(lib.getName()));
                librariesString = librariesString.append(libraryProvider.getLibraryFile(lib).getAbsolutePath()).append(
                        LIBRARY_SEPARATOR);
            }
        }
        //// mods can inject JARs after libraries
        if(moddingProfileSpecified) {
            File[] customFiles = mods.injectAfterLibs(LIBRARY_SEPARATOR);
            if(customFiles != null) {
                MCLauncherAPI.log.fine("Injecting custom libraries after library list");
                for (File file : customFiles) {
                    librariesString.append(file.getAbsolutePath()).append(LIBRARY_SEPARATOR);
                }
            }
        }
        // append the game JAR at the end
        String jarToUse = jarFile.getAbsolutePath();
        if(moddingProfileSpecified && mods.getCustomGameJar() != null) {
            MCLauncherAPI.log.fine("Replacing game JAR");
            jarToUse = mods.getCustomGameJar().getAbsolutePath();
        }
        librariesString = librariesString.append(jarToUse);
        // append the whole classpath to command
        command.add(librariesString.toString());

        // look for the main class
        String mainClass = version.getMainClass();
        if(moddingProfileSpecified && mods.getMainClass() != null){
            MCLauncherAPI.log.fine("Replacing main class");
            mainClass = mods.getMainClass();
        }
        command.add(mainClass);
        // create minecraft arguments
        List<String> arguments = getArguments(version.getGameArgs(), mc, resourcesInstaller.getAssetsDirectory(), session, settings,
                version);
        // give mods opportunity to change minecraft arguments
        if(moddingProfileSpecified){
            List<String> args = mods.changeMinecraftArguments(arguments);
            if(args != null) {
                arguments = args;
                MCLauncherAPI.log.fine("Replacing minecraft arguments");
            }
        }

        // now append all minecraft arguments to the command
        for (String arg : arguments) {
            command.add(arg);
        }
        // if server is specified, append --server and --port
        if (server != null) {
            command.add("--server");
            command.add(server.getIP());
            command.add("--port");
            command.add("" + server.getPort());
        }
        // mods have final chance to add parameters
        if(moddingProfileSpecified && mods.getLastParameters() != null){
            command.addAll(mods.getLastParameters());
            MCLauncherAPI.log.fine("Adding last parameters after the entire command");
        }
        MCLauncherAPI.log.fine("Launching command is now ready.");
        MCLauncherAPI.log.fine(command.toString());
        return command;
    }
}
