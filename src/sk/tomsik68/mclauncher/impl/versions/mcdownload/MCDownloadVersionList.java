package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.impl.common.Observable;
import sk.tomsik68.mclauncher.util.HttpUtils;

public class MCDownloadVersionList extends Observable<IVersion> implements IVersionList {
    private static final String VERSION_LIST_URL = "http://s3.amazonaws.com/Minecraft.Download/versions/versions.json";
    private static final String VERSION_URL = "http://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.json";

    @Override
    public void startDownload() throws Exception {
        String jsonString = HttpUtils.httpGet(VERSION_LIST_URL);
        JSONObject versionInformation = (JSONObject) JSONValue.parse(jsonString);
        JSONArray versions = (JSONArray) versionInformation.get("versions");
        for (Object object : versions) {
            JSONObject versionObject = (JSONObject) object;
            String fullVersionString = HttpUtils.httpGet(VERSION_URL.replace("<VERSION>", versionObject.get("id").toString()));
            JSONObject fullVersionObject = (JSONObject) JSONValue.parse(fullVersionString);
            MCDownloadVersion version = new MCDownloadVersion(fullVersionObject);
            notifyObservers(version);
        }
    }
}
