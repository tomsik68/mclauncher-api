package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.api.versions.LatestVersionInformation;
import sk.tomsik68.mclauncher.impl.common.Observable;
import sk.tomsik68.mclauncher.util.HttpUtils;

final class MCDownloadOnlineVersionList extends Observable<String> implements IVersionList{

    private static final String JSONVERSION_LIST_URL = "http://s3.amazonaws.com/Minecraft.Download/versions/versions.json";
    private static final String FULL_VERSION_URL_TEMPLATE = "http://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.json";

    MCDownloadOnlineVersionList(){
    }

    @Override
    public void startDownload() throws Exception {
        // at first, we download the complete version list
        String jsonString = HttpUtils.httpGet(JSONVERSION_LIST_URL);
        JSONObject versionInformation = (JSONObject) JSONValue.parse(jsonString);
        JSONArray versions = (JSONArray) versionInformation.get("versions");
        // and then, for each version...
        for (Object object : versions) {
            JSONObject versionObject = (JSONObject) object;
            notifyObservers(versionObject.get("id").toString());
        }
    }

    @Override
    public IVersion retrieveVersionInfo(String id) throws Exception{
        String fullVersionJSONString = HttpUtils.httpGet(FULL_VERSION_URL_TEMPLATE.replace("<VERSION>", id));
        JSONObject fullVersionObject = (JSONObject) JSONValue.parse(fullVersionJSONString);
        // ,create a MCDownloadVersion based on it
        MCDownloadVersion version = new MCDownloadVersion(fullVersionObject);
        return version;
    }

    @Override
    public LatestVersionInformation getLatestVersionInformation() throws Exception {
        String jsonString = HttpUtils.httpGet(JSONVERSION_LIST_URL);
        JSONObject versionInformation = (JSONObject) JSONValue.parse(jsonString);
        JSONObject latest = (JSONObject)versionInformation.get("latest");
        LatestVersionInformation result = new LatestVersionInformation(latest.get("release").toString(), latest.get("snapshot").toString());
        return result;
    }
}
