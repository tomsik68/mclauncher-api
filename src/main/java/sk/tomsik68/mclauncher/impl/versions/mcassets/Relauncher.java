package sk.tomsik68.mclauncher.impl.versions.mcassets;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map.Entry;

@Deprecated
final class Relauncher {
    public static void main(String[] args) {
        try {
            HashMap<String, String> arguments = new HashMap<String, String>();
            arguments.put("-dm", "false");
            arguments.put("-ap", "false");
            arguments.put("-m", "false");
            arguments.put("-w", "800");
            arguments.put("-h", "600");
            arguments.put("-dlt", "deprecated");
            arguments.put("-lv", "deprecated");
            for (int i = 0; i < args.length; ++i) {
                if ((args[i].equalsIgnoreCase("--user-name") || args[i].equalsIgnoreCase("-un")) && (i + 1) < args.length) {
                    arguments.put("-un", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-sid") && (i + 1) < args.length) {
                    arguments.put("-sid", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-dm")) {
                    arguments.put("-dm", "true");
                } else if (args[i].equalsIgnoreCase("-ap")) {
                    arguments.put("-ap", "true");
                } else if (args[i].equalsIgnoreCase("-dir") && (i + 1) < args.length) {
                    arguments.put("-dir", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-jar") && (i + 1) < args.length) {
                    arguments.put("-jar", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-lib") && (i + 1) < args.length) {
                    arguments.put("-lib", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-mp") && (i + 1) < args.length) {
                    arguments.put("-mp", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-w") && (i + 1) < args.length) {
                    arguments.put("-w", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-h") && (i + 1) < args.length) {
                    arguments.put("-h", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-m")) {
                    arguments.put("-m", "true");
                } else if (args[i].equalsIgnoreCase("-lwjgl") && (i + 1) < args.length) {
                    arguments.put("-lwjgl", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-jlibpath") && (i + 1) < args.length) {
                    arguments.put("-jlibpath", args[i + 1]);
                } else if (args[i].equalsIgnoreCase("-args") && (i + 1) < args.length) {
                    arguments.put("-args", args[i + 1]);
                }

            }
            System.out.println("ArgumentMap built!");
            for (Entry<String, String> argument : arguments.entrySet()) {
                System.out.println(argument.getKey() + ": '" + argument.getValue() + "'");
            }
            String userName = arguments.get("-un");
            String sessionID = arguments.get("-sid");
            String lastVer = arguments.get("-lv");
            String dlTicket = arguments.get("-dlt");
            File gameDir = new File(arguments.get("-dir"));
            File gameFile = new File(arguments.get("-jar"));
            String[] sLibraries = arguments.get("-lib").split(";");
            File[] libraries = new File[sLibraries.length];
            for (int j = 0; j < sLibraries.length; j++) {
                libraries[j] = new File(sLibraries[j]);
            }
            int w = Integer.parseInt(arguments.get("-w"));
            int h = Integer.parseInt(arguments.get("-h"));
            boolean maximize = Boolean.parseBoolean(arguments.get("-m"));
            boolean changeOptions = Boolean.parseBoolean(arguments.get("-ap"));
            CustomClassLoader loader = new CustomClassLoader();
            loader.addJAR(gameFile.toURI().toURL());
            for (File lib : libraries) {
                loader.addJAR(lib.toURI().toURL());
            }
            System.setProperty("org.lwjgl.librarypath", new File(arguments.get("-lwjgl")).getAbsolutePath());
            System.setProperty("net.java.games.input.librarypath", new File(arguments.get("-jlibpath")).getAbsolutePath());
            LauncherComponent launcher = new LauncherComponent(loader);
            launcher.setParameter("username", userName);
            launcher.setParameter("sessionid", sessionID);
            launcher.setParameter("downloadticket", dlTicket);
            launcher.setParameter("latestversion", lastVer);
            launcher.setParameter("stand-alone", "true");
            launcher.setParameter("demo", arguments.get("-dm"));
            /*
             * if(options.has(multiplayerOption)){ String ipPort =
             * options.valueOf(multiplayerOption);
             */
            if (arguments.containsKey("-mp")) {
                final String ipPort = arguments.get("-mp");
                final String ip = ipPort.split(":")[0];
                String port = "25565";
                if (ipPort.contains(":"))
                    port = ipPort.split(":")[1];
                System.out.println("MP: " + ipPort);
                launcher.setParameter("server", ip);
                launcher.setParameter("port", port);
            }
            // }
            // if(options.has(gameDirOption)){
            if (arguments.containsKey("-dir")) {
                Class<?> mcClass = loader.findClass("net.minecraft.client.Minecraft");
                Field[] fields = mcClass.getDeclaredFields();
                for (Field field : fields) {
                    if (field.getType() == File.class && Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        field.set(null, gameDir);
                        break;
                    }
                }
            }
            // }
            /*
             * if(options.has(argsOption)){ JSONObject params = (JSONObject)
             * JSONValue.parse(options.valueOf(argsOption));
             */
            JSONObject params = null;
            if (arguments.containsKey("-args")) {
                params = (JSONObject) JSONValue.parse(arguments.get("-args"));
                launcher.setAll(params);
            }
            JFrame frame = new JFrame();
            frame.setSize(w, h);
            // TODO change title feature
            frame.setTitle(gameFile.getName() + " running via MCLauncherAPI");
            launcher.setSize(w, h);
            if (maximize)
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setLayout(new BorderLayout());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(launcher);
            frame.setVisible(true);
            if (changeOptions) {
                launcher.start();
            } else
                launcher.startMinecraft();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
