package sk.tomsik68.mclauncher.impl.versions.mcdownload.assets;

import java.io.File;
import java.io.FileReader;
import java.util.Map.Entry;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersion;
import sk.tomsik68.mclauncher.util.FilePathBuilder;
import sk.tomsik68.mclauncher.util.FileUtils;

public class MCDResourcesInstaller {

    private final File indexesDir, objectsDir, virtualDir;

    public MCDResourcesInstaller(IMinecraftInstance mc) {
        indexesDir = new File(mc.getAssetsDirectory(), "indexes");
        objectsDir = new File(mc.getAssetsDirectory(), "objects");
        virtualDir = new File(mc.getAssetsDirectory(), "virtual");
    }

    public void install(MCDownloadVersion version, IProgressMonitor progress) throws Exception {
        String index = version.getAssetsIndexName();
        File indexDest = new File(indexesDir, index + ".json");
        String indexDownloadURL = MCLauncherAPI.URLS.RESOURCES_INDEX_URL + index + ".json";
        if (!indexDest.exists() || indexDest.length() == 0)
            FileUtils.downloadFileWithProgress(indexDownloadURL, indexDest, null);
        JSONObject jsonAssets = (JSONObject) JSONValue.parse(new FileReader(indexDest));
        AssetIndex assets = new AssetIndex(jsonAssets);
        MCLauncherAPI.log.info("Retrieving objects...");
        if (!assets.isVirtual()) {
            downloadAssetList(assets, progress);
        } else
            downloadVirtualAssetList(assets, index, progress);
    }

    private void downloadVirtualAssetList(AssetIndex assets, String assetsName, IProgressMonitor progress) throws Exception {
        File assetsDir = new File(virtualDir, assetsName);
        assetsDir.mkdirs();
        String path;
        File dest;

        for (Entry<String, Asset> entry : assets.getAssets().entrySet()) {
            MCLauncherAPI.log.info("Retrieving " + entry.getKey());
            path = entry.getKey().replace('/', File.separatorChar);
            dest = new File(assetsDir, path);

            dest.getParentFile().mkdirs();
            if (!dest.exists() || dest.length() != entry.getValue().getSize())
                FileUtils.downloadFileWithProgress(entry.getValue().getUrl(), dest, progress);
            else
                MCLauncherAPI.log.info("Skipping already downloaded " + entry.getKey());
        }
    }

    private void downloadAssetList(AssetIndex assets, IProgressMonitor progress) throws Exception {
        FilePathBuilder pathBuilder;
        for (Entry<String, Asset> entry : assets.getAssets().entrySet()) {
            MCLauncherAPI.log.info("Retrieving " + entry.getKey());
            pathBuilder = new FilePathBuilder(objectsDir);
            pathBuilder.append(entry.getValue().getPreHash()).append(entry.getValue().getHash());
            File dest = pathBuilder.getResult();
            if (!dest.exists() || dest.length() != entry.getValue().getSize())
                FileUtils.downloadFileWithProgress(entry.getValue().getUrl(), dest, progress);
            else
                MCLauncherAPI.log.info("Skipping already downloaded " + entry.getKey());
        }
    }

}
