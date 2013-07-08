package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.awt.BorderLayout;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import javax.swing.JFrame;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.impl.common.Platform;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Relauncher {
    public static void main(String[] args) {
        for(String s : args){
            System.out.print(s+",");
        }
        try {
            OptionParser parser = new OptionParser();
            ArgumentAcceptingOptionSpec<String> userNameOption = parser.acceptsAll(Arrays.asList("un", "user-name")).withRequiredArg().ofType(String.class).required();
            ArgumentAcceptingOptionSpec<String> sessionIdOption = parser.acceptsAll(Arrays.asList("sid", "session-id")).withRequiredArg().ofType(String.class).required();
            ArgumentAcceptingOptionSpec<String> downloadTicketOption = parser.acceptsAll(Arrays.asList("dlt", "download-ticket")).withRequiredArg().ofType(String.class).defaultsTo("deprecated");
            ArgumentAcceptingOptionSpec<String> lastVersionOption = parser.acceptsAll(Arrays.asList("lv", "latest-version")).withRequiredArg().ofType(String.class).required();
            ArgumentAcceptingOptionSpec<Boolean> demoOption = parser.acceptsAll(Arrays.asList("dm", "demo-game")).withOptionalArg().ofType(Boolean.class).defaultsTo(false);
            ArgumentAcceptingOptionSpec<Boolean> changeOptionsOption = parser.acceptsAll(Arrays.asList("ap", "applet-parameters")).withOptionalArg().ofType(Boolean.class).defaultsTo(false);
            ArgumentAcceptingOptionSpec<File> gameDirOption = parser.acceptsAll(Arrays.asList("dir", "game-directory")).withRequiredArg().ofType(File.class).defaultsTo(Platform.getCurrentPlatform().getWorkingDirectory());
            ArgumentAcceptingOptionSpec<File> gameJarOption = parser.acceptsAll(Arrays.asList("jar", "game-file")).withRequiredArg().ofType(File.class).required();
            ArgumentAcceptingOptionSpec<File[]> gameFilesOption = parser.acceptsAll(Arrays.asList("lib", "game-libraries")).withRequiredArg().ofType(File[].class).required();
            ArgumentAcceptingOptionSpec<String> multiplayerOption = parser.acceptsAll(Arrays.asList("mp", "multiplayer")).withRequiredArg().ofType(String.class);
            ArgumentAcceptingOptionSpec<Integer> widthOption = parser.acceptsAll(Arrays.asList("w", "window-width")).withRequiredArg().ofType(Integer.class).defaultsTo(800);
            ArgumentAcceptingOptionSpec<Integer> heightOption = parser.acceptsAll(Arrays.asList("h", "window-height")).withRequiredArg().ofType(Integer.class).defaultsTo(600);
            ArgumentAcceptingOptionSpec<Boolean> maximizeOption = parser.acceptsAll(Arrays.asList("m", "maximize-window")).withOptionalArg().ofType(Boolean.class).defaultsTo(false);
            ArgumentAcceptingOptionSpec<String> argsOption = parser.acceptsAll(Arrays.asList("args","additional-parameters")).withRequiredArg().ofType(String.class);
            
            OptionSet options = parser.parse(args);

            String userName = options.valueOf(userNameOption);
            String sessionID = options.valueOf(sessionIdOption);
            String lastVer = options.valueOf(lastVersionOption);
            String dlTicket = options.valueOf(downloadTicketOption);
            
            File gameDir = options.valueOf(gameDirOption);
            int w = options.valueOf(widthOption);
            int h = options.valueOf(heightOption);
            boolean maximize = options.valueOf(maximizeOption);
            boolean demo = options.valueOf(demoOption);
            boolean changeOptions = options.valueOf(changeOptionsOption);
            File gameFile = options.valueOf(gameJarOption);
            File[] libraries = options.valueOf(gameFilesOption);
            
            CustomClassLoader loader = new CustomClassLoader();
            loader.addJAR(gameFile.toURI().toURL());
            for(File lib : libraries){
                loader.addJAR(lib.toURI().toURL());
            }
            
            LauncherComponent launcher = new LauncherComponent(loader);
            launcher.setParameter("username", userName);
            launcher.setParameter("sessionid", sessionID);
            launcher.setParameter("downloadticket", dlTicket);
            launcher.setParameter("latestversion", lastVer);
            launcher.setParameter("stand-alone", "true");
            launcher.setParameter("demo", ""+demo);
            if(options.has(multiplayerOption)){
                String ipPort = options.valueOf(multiplayerOption);
                String ip = ipPort.split(":")[0];
                String port = "25565";
                if(ipPort.contains(":"))
                    port = ipPort.split(":")[1];
                launcher.setParameter("server", ip);
                launcher.setParameter("port", port);
            }
            if(options.has(gameDirOption)){
                Class<?> mcClass = loader.findClass("net.minecraft.client.Minecraft");
                Field[] fields = mcClass.getDeclaredFields();
                for(Field field : fields){
                    if(field.getType() == File.class && Modifier.isStatic(field.getModifiers())){
                        field.setAccessible(true);
                        field.set(null, gameDir);
                        break;
                    }
                }
            }
            if(options.has(argsOption)){
                JSONObject params = (JSONObject) JSONValue.parse(options.valueOf(argsOption));
                launcher.setAll(params);
            }
            JFrame frame = new JFrame();
            frame.setSize(w, h);
            if (maximize)
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(launcher);
            if(changeOptions){
                launcher.start();
            }else
                launcher.startMinecraft();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
