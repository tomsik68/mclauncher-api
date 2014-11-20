package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.impl.common.Observable;
import sk.tomsik68.mclauncher.util.HttpUtils;

public class MCDownloadVersionList extends Observable<IVersion> implements IVersionList {


    @Override
    public void startDownload() throws Exception {
        String jsonString = HttpUtils.httpGet(MCLauncherAPI.URLS.JSONVERSION_LIST_URL);
        JSONObject versionInformation = (JSONObject) JSONValue.parse(jsonString);
        JSONArray versions = (JSONArray) versionInformation.get("versions");
        for (Object object : versions) {
            JSONObject versionObject = (JSONObject) object;
            String fullVersionString = HttpUtils.httpGet(MCLauncherAPI.URLS.NEW_VERSION_URL.replace("<VERSION>", versionObject.get("id").toString()));
            JSONObject fullVersionObject = (JSONObject) JSONValue.parse(fullVersionString);
            MCDownloadVersion version = new MCDownloadVersion(fullVersionObject);
            notifyObservers(version);
        }
    }
}
