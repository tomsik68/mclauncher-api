package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionInstallListener;
import sk.tomsik68.mclauncher.api.versions.IVersionInstaller;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.util.ExtractUtils;
import sk.tomsik68.mclauncher.util.FileUtils;

public class MCAssetsVersionInstaller implements IVersionInstaller {
    private final ArrayList<IVersionInstallListener> listeners = new ArrayList<IVersionInstallListener>();
    private static final String LWJGL_DOWNLOAD_URL = "http://kent.dl.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%202.9.0/lwjgl-2.9.0.zip";
    private static final String RESOURCES_DOWNLOAD_URL = "http://s3.amazonaws.com/MinecraftResources/";
    
    public MCAssetsVersionInstaller() {

    }

    @Override
    public void install(IVersion version, IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        String url = getVersionURL(version.getId());
        mc.getJarProvider().prepareVersionInstallation(version);
        if (!mc.getJarProvider().getVersionFile(version.getUniqueID()).exists())
            FileUtils.downloadFileWithProgress(url, mc.getJarProvider().getVersionFile(version.getUniqueID()), progress);

        File[] lwjgl = mc.getLibraryProvider().getDefaultLWJGLJars();
        boolean update = false;
        for (File file : lwjgl) {
            update = update || !file.exists();
        }
        update = update || !mc.getLibraryProvider().getNativesDirectory().exists();
        if (update) {
            updateJARs(mc, progress);
        }
        updateResources(mc.getLocation(), progress);
        for (IVersionInstallListener listener : listeners) {
            listener.versionInstalled(version);
        }

    }

    private void updateResources(File mcLocation, IProgressMonitor progress) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(RESOURCES_DOWNLOAD_URL);
        for (int i = 0; i < doc.getElementsByTagName("ListBucketResult").item(0).getChildNodes().getLength(); i++) {
            Node node = doc.getElementsByTagName("ListBucketResult").item(0).getChildNodes().item(i);
            if ((node != null) && ("Contents".equalsIgnoreCase(node.getNodeName())) && (node.getChildNodes().getLength() > 0))
                if (("Key".equals(node.getFirstChild().getNodeName()))) {
                    String toDL = node.getFirstChild().getTextContent();
                    File dest = new File(mcLocation, "resources"+File.separator+toDL.replace('/', File.separatorChar));
                    if (!dest.exists()) {
                        dest.mkdirs();
                        if (!toDL.endsWith("/")) {
                            dest.delete();
                            FileUtils.downloadFileWithProgress(RESOURCES_DOWNLOAD_URL + toDL.replace(" ", "%20"), dest, progress);
                        }
                    }
                }
        }
    }

    private void updateJARs(IMinecraftInstance mc, IProgressMonitor progress) throws Exception {
        File lwjglDir = new File(mc.getLocation(), "lwjgl-2.9.0");
        lwjglDir.deleteOnExit();
        File dest = new File(mc.getJarProvider().getBinFolder(), "lwjgl.zip");
        FileUtils.downloadFileWithProgress(LWJGL_DOWNLOAD_URL, dest, progress);
        mc.getLibraryProvider().getNativesDirectory().mkdirs();
        dest.deleteOnExit();
        ExtractUtils.extractZipWithoutRules(dest, mc.getLocation());
        File[] lwjgl = mc.getLibraryProvider().getDefaultLWJGLJars();
        // move JARs from LWJGL
        for (File file : lwjgl) {
            FileUtils.copyFile(new File(lwjglDir + File.separator + "jar", file.getName()), file);
        }
        // move natives
        File[] nativeThings = new File(lwjglDir, "native" + File.separator + Platform.getCurrentPlatform().getMinecraftName()).listFiles();
        for (File file : nativeThings) {
            FileUtils.copyFile(file, new File(mc.getLibraryProvider().getNativesDirectory(), file.getName()));
        }
    }

    private String getVersionURL(String id) {
        return "http://assets.minecraft.net/" + id + "/minecraft.jar";
    }

    @Override
    public void addVersionInstallListener(IVersionInstallListener listener) {
        listeners.add(listener);
    }

}
