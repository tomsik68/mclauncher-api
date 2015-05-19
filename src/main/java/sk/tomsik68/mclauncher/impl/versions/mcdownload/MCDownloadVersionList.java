package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import sk.tomsik68.mclauncher.api.versions.IVersion;
import sk.tomsik68.mclauncher.api.versions.IVersionList;
import sk.tomsik68.mclauncher.impl.common.Observable;
import sk.tomsik68.mclauncher.util.HttpUtils;

import java.util.HashMap;
import java.util.HashSet;

public final class MCDownloadVersionList extends Observable<IVersion> implements IVersionList {
    private static final String JSONVERSION_LIST_URL = "http://s3.amazonaws.com/Minecraft.Download/versions/versions.json";
    private static final String FULL_VERSION_URL_TEMPLATE = "http://s3.amazonaws.com/Minecraft.Download/versions/<VERSION>/<VERSION>.json";
    private HashMap<String, MCDownloadVersion> versionMap = new HashMap<String, MCDownloadVersion>();
    private HashSet<String> resolved = new HashSet<String>();

    @Override
    public void startDownload() throws Exception {
        versionMap.clear();
        // at first, we download the complete version list
        String jsonString = HttpUtils.httpGet(JSONVERSION_LIST_URL);
        JSONObject versionInformation = (JSONObject) JSONValue.parse(jsonString);
        JSONArray versions = (JSONArray) versionInformation.get("versions");
        // and then, for each version...
        for (Object object : versions) {
            JSONObject versionObject = (JSONObject) object;
            // ... we download the full version file (1.8.json etc)
            String fullVersionJSONString = HttpUtils.httpGet(FULL_VERSION_URL_TEMPLATE.replace("<VERSION>", versionObject.get("id").toString()));
            JSONObject fullVersionObject = (JSONObject) JSONValue.parse(fullVersionJSONString);
            // ,create a MCDownloadVersion based on it
            MCDownloadVersion version = new MCDownloadVersion(fullVersionObject);
            // and save it inside the version map
            versionMap.put(version.getId(), version);
        }

        // resolve inheritsFrom in versions
        for(MCDownloadVersion version : versionMap.values()){
            // if version needs to get dependencies
            if(version.getInheritsFrom() != null && !version.isInherited()){
                resolveInheritance(version);
            }
        }
    }

    private void resolveInheritance(MCDownloadVersion version){
        // parent's parent needs to be resolved first
        MCDownloadVersion parent = versionMap.get(version.getInheritsFrom());
        if(parent.getInheritsFrom() != null)
            resolveInheritance(parent);
        else {
            version.doInherit(parent);
        }
    }
}
