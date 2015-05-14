package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
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

public class MCDownloadVersionLauncher implements IVersionLauncher {

    public String[] getMinecraftArguments(IMinecraftInstance mc,
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
        subst.setVariable("game_assets", mc.getAssetsDirectory()
                .getAbsolutePath());
        subst.setVariable("assets_root", mc.getAssetsDirectory()
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
                                         IMinecraftInstance mc, ServerInfo server, IVersion v,
                                         ILaunchSettings settings, IModdingProfile mods) throws Exception {
        // get JSON information about the version
        File jsonFile = new File(mc.getJarProvider().getVersionFile(v)
                .getParent(), "info.json");
        System.out.println("Looking for " + jsonFile.getAbsolutePath());
        if (!jsonFile.exists()) {
            throw new FileNotFoundException(
                    "You need to download the version at first! (JSON description file not found!)");
        }
        MCDownloadVersion version = new MCDownloadVersion(
                (JSONObject) JSONValue.parse(new FileInputStream(jsonFile)));
        File jarFile = mc.getJarProvider().getVersionFile(version);
        if (!version.isCompatible()) {
            throw new VersionIncompatibleException(version);
        }
        if (version.getMinimumLauncherVersion() > MCLauncherAPI.MC_LAUNCHER_VERSION) {
            throw new RuntimeException(
                    "You need to update MCLauncher-API to run this minecraft version! Required API version: "
                            + version.getMinimumLauncherVersion());
        }
        ArrayList<String> command = new ArrayList<String>();
        if (settings.getCommandPrefix() != null)
            command.addAll(settings.getCommandPrefix());
        if (settings.getJavaLocation() != null)
            command.add(settings.getJavaLocation().getAbsolutePath());
        else
            command.add("java");
        if (settings.getInitHeap() != null
                && settings.getInitHeap().length() > 0)
            command.add("-Xms".concat(settings.getInitHeap()));
        if (settings.getHeap() != null && settings.getHeap().length() > 0)
            command.add("-Xmx".concat(settings.getHeap()));
        if (settings.getJavaArguments() != null) {
            command.addAll(settings.getJavaArguments());
        }
        File nativesDir = mc.getLibraryProvider().getNativesDirectory(version);
        command.add("-Djava.library.path=" + nativesDir.getAbsolutePath());
        command.add("-cp");
        StringBuilder sb = new StringBuilder();
        final String LIBRARY_SEPARATOR = System.getProperty("path.separator");
        if(mods != null) {
            /*for (File file : mods.getCoreMods()) {
                sb = sb.append(file.getAbsolutePath()).append(LIBRARY_SEPARATOR);
            }*/
            String inject = mods.injectBeforeLibs(LIBRARY_SEPARATOR);
            if(inject != null) {
                if(!inject.endsWith(LIBRARY_SEPARATOR))
                    inject = inject.concat(LIBRARY_SEPARATOR);
                sb = sb.append(inject);

            }
        }
        for (Library lib : version.getLibraries()) {
            if (lib.isCompatible() && (mods == null || mods.isLibraryAllowed(lib))) {
                File libraryFile = mc.getLibraryProvider().getLibraryFile(lib);
                if (!libraryFile.exists()) {
                    throw new FileNotFoundException("Library file wasn't found");
                }
                sb = sb.append(libraryFile.getAbsolutePath()).append(
                        LIBRARY_SEPARATOR);
            }
        }

        if(mods != null) {
            String inject = mods.injectAfterLibs(LIBRARY_SEPARATOR);
            if(inject != null) {
                if(!inject.endsWith(LIBRARY_SEPARATOR))
                    inject = inject.concat(LIBRARY_SEPARATOR);
                sb = sb.append(inject);
            }
        }

        String jarToUse = jarFile.getAbsolutePath();
        if(mods != null && mods.getCustomGameJar() != null) {
            jarToUse = mods.getCustomGameJar();
        }
        sb = sb.append(jarToUse);

        command.add(sb.toString());

        String mainClass = version.getMainClass();
        if(mods != null && mods.getMainClass() != null){
            mainClass = mods.getMainClass();
        }
        command.add(mainClass);
        String[] arguments = getMinecraftArguments(mc, session, settings,
                version);
        if(mods != null){
            String[] args = mods.changeMinecraftArguments(arguments);
            if(args != null)
                arguments = args;
        }
        for (String arg : arguments) {
            command.add(arg);
        }
        if (server != null) {
            command.add("--server");
            command.add(server.getIP());
            command.add("--port");
            command.add("" + server.getPort());
        }
        if(mods != null){
            command.addAll(mods.getLastParameters());
        }

        return command;
    }
}
