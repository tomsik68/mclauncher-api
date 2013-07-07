package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;
import sk.tomsik68.mclauncher.api.common.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.servers.ISavedServer;
import sk.tomsik68.mclauncher.api.versions.IVersionLauncher;

public class MCAssetsVersionLauncher implements IVersionLauncher<MCAssetsVersion>{

    @Override
    public Process launch(ISession session, IMinecraftInstance mc, ISavedServer server, MCAssetsVersion version, ILaunchSettings settings) throws Exception {
        String pathToJar = Relauncher.class.getProtectionDomain().getCodeSource().getLocation().getFile();
        List<String> command = new ArrayList<String>();
        command.addAll(settings.getCommandPrefix());
        command.add("java");
        command.add("-Xms".concat(settings.getInitHeap()));
        command.add("-Xmx".concat(settings.getHeap()));
        command.add("-cp");
        command.add(pathToJar);
        command.add(Relauncher.class.getName());
        command.add("-un");
        command.add(session.getUsername());
        command.add("-sid");
        command.add(session.getSessionID());
        command.add("-dlt");
        command.add(session.getDownloadTicket());
        command.add("-lv");
        command.add(session.getLastVersion());
        command.add("-dir");
        command.add(mc.getLocation().toString());
        command.add("-jar");
        command.add(mc.getJarProvider().getVersionFile(version.getUniqueID()).getPath());
        File[] files = mc.getLibraryProvider().getDefaultLWJGLJars();
        command.add("-lib");
        for(File file : files){
            command.add(file.getPath());
        }
        if(settings.getCustomParameters() != null && settings.getCustomParameters().size() > 0){
            JSONObject params = new JSONObject(settings.getCustomParameters());
            command.add("-args");
            command.add(params.toJSONString(JSONStyle.LT_COMPRESS));
        }
        if(server != null){
            command.add("-mp");
            command.add(server.getIP());
        }
        if(settings.isModifyAppletOptions()){
            command.add("-ap");
            command.add("true");
        }
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(settings.isErrorStreamRedirected());
        return pb.start();
    }

}
