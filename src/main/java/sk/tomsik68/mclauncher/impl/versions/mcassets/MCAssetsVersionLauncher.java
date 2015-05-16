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
        String pathToJar = Relauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        List<String> command = new ArrayList<String>();
        if (settings.getCommandPrefix() != null && !settings.getCommandPrefix().isEmpty())
            command.addAll(settings.getCommandPrefix());
        if (settings.getJavaLocation() == null)
            command.add("java");
        else
            command.add(settings.getJavaLocation().getAbsolutePath());
        if (settings.getJavaArguments() != null && !settings.getJavaArguments().isEmpty())
            command.addAll(settings.getJavaArguments());
        command.add("-Xms".concat(settings.getInitHeap()));
        command.add("-Xmx".concat(settings.getHeap()));
        command.add("-cp");
        command.add(pathToJar);
        command.add(Relauncher.class.getName());
        command.add("-un");
        command.add(session.getUsername());
        command.add("-sid");
        command.add(session.getSessionID());
        command.add("-dir");
        command.add(mc.getLocation().toString());
        command.add("-jar");
        command.add(jarManager.getVersionFile(version).getPath());
        File[] files = MCAssetsVersionInstaller.getDefaultLWJGLJars(mc.getLocation());
        command.add("-lib");
        StringBuilder sb = new StringBuilder();
        for (File file : files) {
            sb = sb.append(file.getPath()).append(';');
        }
        if (sb.length() > 0)
            sb = sb.deleteCharAt(sb.length() - 1);
        command.add(sb.toString());
        if (settings.getCustomParameters() != null && settings.getCustomParameters().size() > 0) {
            JSONObject params = new JSONObject(settings.getCustomParameters());
            command.add("-args");
            command.add(params.toJSONString(JSONStyle.NO_COMPRESS));
        }
        if (server != null) {
            command.add("-mp");
            command.add(server.getIP() + ":" + server.getPort());
        }
        if (settings.isModifyAppletOptions()) {
            command.add("-ap");
            command.add("true");
        }
        command.add("-lwjgl");
        command.add(jarManager.getNativesDirectory().getAbsolutePath());
        command.add("-jlibpath");
        command.add(jarManager.getNativesDirectory().getAbsolutePath());
        if(mods != null){
            MCLauncherAPI.log.warning("You're trying to use mods with MCAssets version. MCAssets versions are deprecated, so mods won't load correctly.");
        }
        return command;
    }

}
