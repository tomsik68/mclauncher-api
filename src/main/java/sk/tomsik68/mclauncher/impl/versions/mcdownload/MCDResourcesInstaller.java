package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.io.File;
import java.io.FileReader;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.common.mc.MinecraftInstance;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.util.FilePathBuilder;
import sk.tomsik68.mclauncher.util.FileUtils;

final class MCDResourcesInstaller {

    private final File assetsDir, indexesDir, objectsDir, virtualDir;
    private static final String RESOURCES_INDEX_URL = "https://s3.amazonaws.com/Minecraft.Download/indexes/";

    MCDResourcesInstaller(MinecraftInstance mc) {
        assetsDir = new File(mc.getLocation(), "assets");
        indexesDir = new File(assetsDir, "indexes");
        objectsDir = new File(assetsDir, "objects");
        virtualDir = new File(assetsDir, "virtual");
    }

    /**
     * Installs resources for given version
     * @param version Version to install resources for
     * @param progress ProgressMonitor
     * @throws Exception Connection and I/O errors
     */
    void installAssetsForVersion(MCDownloadVersion version, IProgressMonitor progress) throws Exception {
        // let's see which asset index is needed by this version
        Artifact index = version.getAssetIndex();
        String indexName = version.getAssetsIndexName();
        MCLauncherAPI.log.fine("Installing asset index ".concat(indexName));
        File indexDest = new File(indexesDir, indexName + ".json");
        // download this asset index
        if (!indexDest.exists() || indexDest.length() == 0)
            FileUtils.downloadFileWithProgress(index.getUrl(), indexDest, progress);
        // parse it from JSON
        FileReader fileReader = new FileReader(indexDest);
        JSONObject jsonAssets = (JSONObject) JSONValue.parse(fileReader);
        fileReader.close();
        AssetIndex assets = AssetIndex.fromJson(indexName, jsonAssets);
        // and download individual assets inside it
        downloadAssetList(assets, progress);
        MCLauncherAPI.log.fine("Finished installing asset index ".concat(indexName));
    }

    File getAssetsDirectory(){ return assetsDir; }

    /**
     * Download all the {@link Asset} objects inside this index
     * @param index AssetIndex to download from
     * @param progress
     * @throws Exception
     */
    private void downloadAssetList(AssetIndex index, IProgressMonitor progress) throws Exception {
        for(Asset asset : index.getAssets()){
            MCLauncherAPI.log.info("Updating asset file: ".concat(asset.getKey()).concat(" (size:").concat(""+asset.getSize()).concat("B)"));
            progress.setStatus("Updating asset file: " + asset.getKey());
            
            File dest = getDestFile(index, asset);
            dest.getParentFile().mkdirs();
            if (!dest.exists() || dest.length() != asset.getSize()) {
                MCLauncherAPI.log.finest("Downloading ".concat(asset.getKey()));
                FileUtils.downloadFileWithProgress(asset.getUrl(), dest, progress);
            } else {
                MCLauncherAPI.log.finest("No need to update ".concat(asset.getKey()));
            }
        }
    }

    /**
     *
     * @param index AssetIndex where the asset belongs to
     * @param asset Asset which is going to be downloaded
     * @return Path to file where the asset in given index should be located
     */
    private File getDestFile(AssetIndex index, Asset asset){
        File result;
        // the difference is, that some indexes are virtual.
        // virtual indexes have their assets downloaded to assets/virtual/1.7.3 etc
        if(index.isVirtual()){
            File assetsDir = new File(virtualDir, index.getName());
            assetsDir.mkdirs();
            String path = asset.getKey().replace('/', File.separatorChar);
            result = new File(assetsDir, path);
        } else {
            // while non-virtual indexes download them into 'objects' folder and name them after their hash
            FilePathBuilder pathBuilder = new FilePathBuilder(objectsDir);
            pathBuilder.append(asset.getPreHash()).append(asset.getHash());
            result = pathBuilder.getResult();
        }
        return result;
    }

}
