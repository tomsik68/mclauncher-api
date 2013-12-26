package sk.tomsik68.mclauncher.impl.versions.mcdownload.assets;

import java.io.File;
import java.io.FileReader;
import java.util.Map.Entry;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;

import sk.tomsik68.mclauncher.api.common.mc.IMinecraftInstance;
import sk.tomsik68.mclauncher.impl.versions.mcdownload.MCDownloadVersion;
import sk.tomsik68.mclauncher.util.FilePathBuilder;
import sk.tomsik68.mclauncher.util.FileUtils;

public class MCDResourcesInstaller {
    private final String RESOURCES_INDEX_URL = "https://s3.amazonaws.com/Minecraft.Download/indexes/";
    
    private final IMinecraftInstance mc;
    private final File indexesDir, objectsDir, virtualDir;

    public MCDResourcesInstaller(IMinecraftInstance mc) {
        this.mc = mc;
        indexesDir = new File(mc.getAssetsDirectory(), "indexes");
        objectsDir = new File(mc.getAssetsDirectory(), "objects");
        virtualDir = new File(mc.getAssetsDirectory(), "virtual");
    }

    public void install(MCDownloadVersion version) throws Exception {
        String index = version.getAssetsIndexName();
        File indexDest = new File(indexesDir, index + ".json");
        FileUtils.downloadFileWithProgress(RESOURCES_INDEX_URL+index+".json", indexDest, null);
        JSONObject jsonAssets = (JSONObject) JSONValue.parse(new FileReader(indexDest));
        AssetIndex assets = new AssetIndex(jsonAssets);

        if (!assets.isVirtual()) {
            downloadAssetList(assets);
        } else
            downloadVirtualAssetList(assets);
    }

    private void downloadVirtualAssetList(AssetIndex assets) {

    }

    private void downloadAssetList(AssetIndex assets) throws Exception {
        FilePathBuilder pathBuilder;
        for (Entry<String, Asset> entry : assets.getAssets().entrySet()) {
            pathBuilder = new FilePathBuilder(objectsDir);
            pathBuilder.append(entry.getValue().getPreHash()).append(entry.getValue().getHash());
            File dest = pathBuilder.getResult();
            FileUtils.downloadFileWithProgress(entry.getValue().getUrl(), dest, null);
        }
    }

}
