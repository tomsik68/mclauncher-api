package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;
import sk.tomsik68.mclauncher.util.StringSubstitutor;

public class MCDownloadVersionLauncher implements IVersionLauncher {
    public String[] getMinecraftArguments(IMinecraftInstance mc, ISession session, ILaunchSettings settings, MCDownloadVersion version) {
        // TODO tooo lazy to finish options
        String[] args = version.getMinecraftArgs().split(" ");
        StringSubstitutor subst = new StringSubstitutor("${%s}");
        subst.setVariable("auth_session", session.getSessionID());
        subst.setVariable("auth_access_token", session.getSessionID());
        subst.setVariable("auth_player_name", session.getUsername());
        subst.setVariable("auth_uuid", session.getUUID());
        subst.setVariable("version_name", version.getId());
        subst.setVariable("game_directory", mc.getLocation().getAbsolutePath());
        subst.setVariable("game_assets", mc.getAssetsDirectory().getAbsolutePath());
        subst.setVariable("assets_root", mc.getAssetsDirectory().getAbsolutePath());
        subst.setVariable("assets_index_name", version.getAssetsIndexName());
        subst.setVariable("user_type", session.getType().toString().toLowerCase());
        subst.setVariable("user_properties", "{}");
        for (int i = 0; i < args.length; i++) {
            args[i] = subst.substitute(args[i]);
        }
        return args;
    }

    @Override
    public Process launch(ISession session, IMinecraftInstance mc, ISavedServer server, IVersion v, ILaunchSettings settings) throws Exception {
        // get JSON information about the version
        File jsonFile = new File(mc.getJarProvider().getVersionFile(v.getUniqueID()).getParent(), "info.json");
        if (!jsonFile.exists()) {
            throw new FileNotFoundException("You need to download the version at first! (JSON description file not found!)");
        }
        MCDownloadVersion version = new MCDownloadVersion((JSONObject) JSONValue.parse(new FileInputStream(jsonFile)));
        File jarFile = mc.getJarProvider().getVersionFile(version.getUniqueID());
        if (!version.isCompatible()) {
            throw new VersionIncompatibleException(version);
        }
        if (version.getMinimumLauncherVersion() > MCLauncherAPI.MC_LAUNCHER_VERSION) {
            throw new RuntimeException("You need to update MCLauncher-API to run this minecraft version! Required API version: " + version.getMinimumLauncherVersion());
        }
        ArrayList<String> command = new ArrayList<String>();
        if (settings.getJavaLocation() != null)
            command.add(settings.getJavaLocation().getAbsolutePath());
        else
            command.add("java");
        if (settings.getInitHeap() != null && settings.getInitHeap().length() > 0)
            command.add("-Xms".concat(settings.getInitHeap()));
        if (settings.getHeap() != null && settings.getHeap().length() > 0)
            command.add("-Xmx".concat(settings.getHeap()));
        if (settings.getJavaArguments() != null && !settings.getJavaArguments().isEmpty()) {
            for(String arg : settings.getJavaArguments())
                command.add(arg);
        }
        // TODO minecraft natives
        
        command.add("-Djava.library.path=/home/jasku/dev/Eclipse_workbench/MCLauncherAPI/testmc/bin/natives");
        command.add("-cp");
        StringBuilder sb = new StringBuilder();
        for (Library lib : version.getLibraries()) {
            sb = sb.append(mc.getLibraryProvider().getLibraryFile(lib).getAbsolutePath()).append(':');
        }
        sb = sb.append(jarFile.getAbsolutePath());
        
        command.add(sb.toString());
        command.add(version.getMainClass());
        String[] arguments = getMinecraftArguments(mc, session, settings, version);
        for (String arg : arguments) {
            command.add(arg);
        }
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(settings.isErrorStreamRedirected());
        pb.directory(mc.getLocation());
        System.out.println(command.toString());
        return pb.start();
    }

}
