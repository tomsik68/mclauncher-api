package sk.tomsik68.mclauncher.impl.versions.mcassets;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;
import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.mods.IModdingProfile;
import sk.tomsik68.mclauncher.api.servers.ServerInfo;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public final class MCAssetsVersionLauncher implements IVersionLauncher {

    @Override
    public List<String> getLaunchCommand(ISession session, MinecraftInstance mc, ServerInfo server, IVersion version, ILaunchSettings settings, IModdingProfile mods)
            throws Exception {
        MCAJarManager jarManager = new MCAJarManager(mc);
        // get path to this jar, so that we can relaunch
        String pathToJar = Relauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile();

        List<String> command = new ArrayList<String>();
        // add command prefix from settings
        if (settings.getCommandPrefix() != null && !settings.getCommandPrefix().isEmpty())
            command.addAll(settings.getCommandPrefix());
        // setup java location if present
        if (settings.getJavaLocation() == null)
            command.add("java");
        else
            command.add(settings.getJavaLocation().getAbsolutePath());
        // add java arguments
        if (settings.getJavaArguments() != null && !settings.getJavaArguments().isEmpty())
            command.addAll(settings.getJavaArguments());
        // set memory for java
        command.add("-Xms".concat(settings.getInitHeap()));
        command.add("-Xmx".concat(settings.getHeap()));
        // setup classpath
        command.add("-cp");
        //// this jar
        command.add(pathToJar);
        command.add(Relauncher.class.getName());
        // parameters for relauncher:
        //// username
        command.add("-un");
        command.add(session.getUsername());
        //// session id
        command.add("-sid");
        command.add(session.getSessionID());
        //// game directory
        command.add("-dir");
        command.add(mc.getLocation().toString());
        //// game jar
        command.add("-jar");
        command.add(jarManager.getVersionFile(version).getPath());
        //// natives
        File[] files = MCAssetsVersionInstaller.getDefaultLWJGLJars(mc.getLocation());
        command.add("-lib");
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            sb = sb.append(file.getPath()).append(';');
        }
        if (sb.length() > 0)
            sb = sb.deleteCharAt(sb.length() - 1);
        command.add(sb.toString());
        //// custom parameters for relauncher
        if (settings.getCustomParameters() != null && settings.getCustomParameters().size() > 0) {
            JSONObject params = new JSONObject(settings.getCustomParameters());
            command.add("-args");
            command.add(params.toJSONString(JSONStyle.NO_COMPRESS));
        }
        //// multiplayer server
        if (server != null) {
            command.add("-mp");
            command.add(server.getIP() + ":" + server.getPort());
        }
        //// modify applet options
        if (settings.isModifyAppletOptions()) {
            command.add("-ap");
            command.add("true");
        }
        //// native library path
        command.add("-lwjgl");
        command.add(jarManager.getNativesDirectory().getAbsolutePath());
        command.add("-jlibpath");
        command.add(jarManager.getNativesDirectory().getAbsolutePath());
        // warn dev if they want to use mods on this
        if(mods != null){
            MCLauncherAPI.log.warning("You're trying to use mods with MCAssets version. MCAssets versions are deprecated, so mods won't load correctly.");
        }
        return command;
    }

}
