package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

@Deprecated
public class MCAssetsVersionLauncher implements IVersionLauncher {

    @Override
    public Process launch(ISession session, IMinecraftInstance mc, ISavedServer server, IVersion version, ILaunchSettings settings) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(getLaunchCommand(session, mc, server, version, settings));
        pb.redirectErrorStream(settings.isErrorStreamRedirected());
        Process result = pb.start();
        return result;
    }

    @Override
    public List<String> getLaunchCommand(ISession session, IMinecraftInstance mc, ISavedServer server, IVersion version, ILaunchSettings settings)
            throws Exception {
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
        command.add(mc.getJarProvider().getVersionFile(version).getPath());
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
        command.add(mc.getLibraryProvider().getNativesDirectory(version).getAbsolutePath());
        command.add("-jlibpath");
        command.add(mc.getLibraryProvider().getNativesDirectory(version).getAbsolutePath());
        return command;
    }

}
