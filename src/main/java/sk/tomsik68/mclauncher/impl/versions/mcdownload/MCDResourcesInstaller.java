package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.ui.IProgressMonitor;
import sk.tomsik68.mclauncher.util.FilePathBuilder;
import sk.tomsik68.mclauncher.util.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.util.Map.Entry;

final class MCDResourcesInstaller {

    private final File indexesDir, objectsDir, virtualDir;
    private static final String RESOURCES_INDEX_URL = "https://s3.amazonaws.com/Minecraft.Download/indexes/";

    MCDResourcesInstaller(File assetsDir) {
        indexesDir = new File(assetsDir, "indexes");
        objectsDir = new File(assetsDir, "objects");
        virtualDir = new File(assetsDir, "virtual");
    }

    void installAssetsForVersion(MCDownloadVersion version, IProgressMonitor progress) throws Exception {
        String index = version.getAssetsIndexName();
        File indexDest = new File(indexesDir, index + ".json");
        String indexDownloadURL = RESOURCES_INDEX_URL + index + ".json";
        if (!indexDest.exists() || indexDest.length() == 0)
            FileUtils.downloadFileWithProgress(indexDownloadURL, indexDest, progress);

        JSONObject jsonAssets = (JSONObject) JSONValue.parse(new FileReader(indexDest));
        AssetIndex assets = new AssetIndex(index, jsonAssets);
        downloadAssetList(assets, progress);
    }

    private void downloadAssetList(AssetIndex index, IProgressMonitor progress) throws Exception {
        for(Asset asset : index.getAssets()){
            File dest = getDestFile(index, asset);
            dest.getParentFile().mkdirs();
            if (!dest.exists() || dest.length() != asset.getSize()) {
                FileUtils.downloadFileWithProgress(asset.getUrl(), dest, progress);
            }
        }
    }

    private File getDestFile(AssetIndex index, Asset asset){
        File result;
        if(index.isVirtual()){
            File assetsDir = new File(virtualDir, index.getName());
            assetsDir.mkdirs();
            String path = asset.getKey().replace('/', File.separatorChar);
            result = new File(assetsDir, path);
        } else {
            FilePathBuilder pathBuilder = new FilePathBuilder(objectsDir);
            pathBuilder.append(asset.getPreHash()).append(asset.getHash());
            result = pathBuilder.getResult();
        }
        return result;
    }
}
