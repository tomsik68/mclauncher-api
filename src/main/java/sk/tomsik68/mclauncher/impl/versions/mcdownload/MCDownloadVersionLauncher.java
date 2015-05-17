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
import sk.tomsik68.mclauncher.util.StringSubstitutor;
import sk.tomsik68.mclauncher.api.login.ISession.Prop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

final class MCDownloadVersionLauncher implements IVersionLauncher {

    public String[] getMinecraftArguments(MinecraftInstance mc, File assetsDir,
                                          ISession session, ILaunchSettings settings,
                                          MCDownloadVersion version) {
        // TODO tooo lazy to finish options
        String args = version.getMinecraftArgs();
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

        args = subst.substitute(args);
        return args.split(" ");
    }

    @Override
    public List<String> getLaunchCommand(ISession session,
                                         MinecraftInstance mc, ServerInfo server, IVersion v,
                                         ILaunchSettings settings, IModdingProfile mods) throws Exception {
        MCDResourcesInstaller resourcesInstaller = new MCDResourcesInstaller(mc);
        MCDJarManager jarManager = new MCDJarManager(mc);
        LibraryProvider libraryProvider = new LibraryProvider(mc);
        // get local JSON information about the version
        File jsonFile = jarManager.getInfoFile(v);
        System.out.println("Looking for " + jsonFile.getAbsolutePath());
        if (!jsonFile.exists()) {
            throw new FileNotFoundException(
                    "You need to download the version at first! (JSON description file not found!)");
        }
        // create MCDownloadVersion based on this version
        MCDownloadVersion version = new MCDownloadVersion(
                (JSONObject) JSONValue.parse(new FileInputStream(jsonFile)));
        File jarFile = jarManager.getVersionJAR(version);
        // check if the version is compatible with our OS
        if (!version.isCompatible()) {
            throw new VersionIncompatibleException(version);
        }
        // check if we can launch it using the current version of MCLauncherAPI
        if (version.getMinimumLauncherVersion() > MCLauncherAPI.MC_LAUNCHER_VERSION) {
            throw new RuntimeException(
                    "You need to update MCLauncher-API to run this minecraft version! Required API version: "
                            + version.getMinimumLauncherVersion());
        }
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
        StringBuilder sb = new StringBuilder();
        final String LIBRARY_SEPARATOR = System.getProperty("path.separator");
        //// mods can inject JARs before libraries
        if(mods != null) {
            String inject = mods.injectBeforeLibs(LIBRARY_SEPARATOR);
            if(inject != null) {
                if(!inject.endsWith(LIBRARY_SEPARATOR))
                    inject = inject.concat(LIBRARY_SEPARATOR);
                sb = sb.append(inject);

            }
        }
        //// now add library files
        for (Library lib : version.getLibraries()) {
            // each library has to be compatible, installed and allowed by modding profile
            if (lib.isCompatible() && (mods == null || mods.isLibraryAllowed(lib.getName()))) {
                if (!libraryProvider.isInstalled(lib)) {
                    throw new FileNotFoundException("Library file wasn't found");
                }
                sb = sb.append(libraryProvider.getLibraryFile(lib).getAbsolutePath()).append(
                        LIBRARY_SEPARATOR);
            }
        }
        //// mods can inject JARs after libraries
        if(mods != null) {
            String inject = mods.injectAfterLibs(LIBRARY_SEPARATOR);
            if(inject != null) {
                if(!inject.endsWith(LIBRARY_SEPARATOR))
                    inject = inject.concat(LIBRARY_SEPARATOR);
                sb = sb.append(inject);
            }
        }
        // append the game JAR at the end
        String jarToUse = jarFile.getAbsolutePath();
        if(mods != null && mods.getCustomGameJar() != null) {
            jarToUse = mods.getCustomGameJar();
        }
        sb = sb.append(jarToUse);
        // append the whole classpath to command
        command.add(sb.toString());

        // look for the main class
        String mainClass = version.getMainClass();
        if(mods != null && mods.getMainClass() != null){
            mainClass = mods.getMainClass();
        }
        command.add(mainClass);
        // create minecraft arguments
        String[] arguments = getMinecraftArguments(mc, resourcesInstaller.getAssetsDirectory(), session, settings,
                version);
        // give mods opportunity to change minecraft arguments
        if(mods != null){
            String[] args = mods.changeMinecraftArguments(arguments);
            if(args != null)
                arguments = args;
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
        if(mods != null){
            command.addAll(mods.getLastParameters());
        }

        return command;
    }
}
