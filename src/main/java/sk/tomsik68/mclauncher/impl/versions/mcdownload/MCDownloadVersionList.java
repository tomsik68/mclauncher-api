package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.impl.common.Observable;
import sk.tomsik68.mclauncher.util.HttpUtils;

public final class MCDownloadVersionList extends Observable<IVersion> implements IVersionList {
    private static final String JSONVERSION_LIST_URL = "http://s3.amazonaws.com/Minecraft.Download/versions/versions.json";
    private static final String FULL_VERSION_URL_TEMPLATE = "http://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.json";

    @Override
    public void startDownload() throws Exception {
        String jsonString = HttpUtils.httpGet(JSONVERSION_LIST_URL);
        JSONObject versionInformation = (JSONObject) JSONValue.parse(jsonString);
        JSONArray versions = (JSONArray) versionInformation.get("versions");
        for (Object object : versions) {
            JSONObject versionObject = (JSONObject) object;

            String fullVersionJSONString = HttpUtils.httpGet(FULL_VERSION_URL_TEMPLATE.replace("<VERSION>", versionObject.get("id").toString()));
            JSONObject fullVersionObject = (JSONObject) JSONValue.parse(fullVersionJSONString);

            MCDownloadVersion version = new MCDownloadVersion(fullVersionObject);
            notifyObservers(version);
        }
    }
}
