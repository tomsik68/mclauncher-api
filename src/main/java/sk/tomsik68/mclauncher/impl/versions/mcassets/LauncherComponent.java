package sk.tomsik68.mclauncher.impl.versions.mcassets;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

@Deprecated
final class LauncherComponent extends Applet implements AppletStub {
    private static final long serialVersionUID = -6942044817024235085L;
    private final ClassLoader loader;
    private final JSplitPane splitPane;
    private final DefaultTableModel model;
    protected boolean active = false;
    private Applet minecraft;
    private HashMap<String, Object> params = new HashMap<String, Object>();

    public LauncherComponent(URLClassLoader loader) {
        System.setProperty("minecraft.applet.WrapperClass", getClass().getName());
        this.loader = loader;
        this.params.put("fullscreen", "false");
        setLayout(new BorderLayout());
        this.splitPane = new JSplitPane();
        this.splitPane.setEnabled(false);
        this.splitPane.setResizeWeight(1.0D);
        this.splitPane.setOrientation(0);
        this.model = new DefaultTableModel();
        this.model.addColumn("Key");
        this.model.addColumn("Value");
        this.model.setRowCount(5);
        JTable tbParameters = new JTable(this.model);
        this.splitPane.setLeftComponent(tbParameters);

        JButton btStart = new JButton("Start Minecraft");
        btStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                LauncherComponent.this.startMinecraft();
            }
        });
        this.splitPane.setRightComponent(btStart);
    }

    public void createApplet() {
        try {
            this.minecraft = ((Applet) this.loader.loadClass("net.minecraft.client.MinecraftApplet").newInstance());
            this.minecraft.setStub(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(Graphics g) {
        if (this.minecraft != null)
            this.minecraft.paint(g);
    }

    public URL getDocumentBase() {
        try {
            return new URL("http://www.minecraft.net/game/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getParameter(String name) {
        if (this.params.containsKey(name)) {
            return this.params.get(name).toString();
        }
        try {
            String superValue = super.getParameter(name);
            if (superValue != null) {
                this.model.addRow(new Object[]{name, superValue});
                return superValue;
            }
        } catch (Exception e) {
            this.params.put(name, "");

            this.model.addRow(new Object[]{name, ""});
        }
        return null;
    }

    public void setParameter(String k, String v) {
        this.params.put(k, v);
        this.model.addRow(new Object[]{k, v});
    }

    public void replace(Applet applet) {
        this.minecraft = applet;
        applet.setStub(this);
        applet.setSize(getWidth(), getHeight());

        setLayout(new BorderLayout());
        add(applet, "Center");

        applet.init();
        this.active = true;
        applet.start();
        validate();
    }

    public void startMinecraft() {
        Thread thread = new Thread() {
            public void run() {
                while (LauncherComponent.this.active) {
                    LauncherComponent.this.repaint();
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        for (int i = 0; i < this.model.getRowCount(); i++) {
            if ((this.model.getValueAt(i, 0) != null) && (this.model.getValueAt(i, 1) != null))
                this.params.put(this.model.getValueAt(i, 0).toString(), this.model.getValueAt(i, 1).toString());
        }
        if (this.minecraft == null)
            createApplet();
        replace(this.minecraft);
    }

    public void start() {
        add(this.splitPane);
    }

    public void appletResize(int width, int height) {
        setSize(width, height);
        this.minecraft.setSize(width, height);
    }

    public boolean isActive() {
        return this.active;
    }

    public void stop() {
        this.active = false;
    }

    public void setAll(Map<String, Object> params2) {
        params.putAll(params2);
    }
}