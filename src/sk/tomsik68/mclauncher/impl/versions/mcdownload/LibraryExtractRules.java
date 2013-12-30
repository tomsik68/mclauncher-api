package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import java.util.ArrayList;
import java.util.zip.ZipEntry;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import sk.tomsik68.mclauncher.util.IExtractRules;

public class LibraryExtractRules implements IExtractRules {
    private ArrayList<String> exclude = new ArrayList<String>();

    public LibraryExtractRules(JSONObject object) {
        JSONArray excludeArray = (JSONArray) object.get("exclude");
        for (Object obj : excludeArray) {
            exclude.add(obj.toString());
        }
    }

    @Override
    public boolean accepts(ZipEntry entry) {
        String path = entry.getName();
        if (exclude != null && !exclude.isEmpty()) {
            for (String p : exclude) {
                if (path.startsWith(p))
                    return false;

            }
        }
        return true;
    }

}
