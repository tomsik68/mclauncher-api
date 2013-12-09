package sk.tomsik68.mclauncher.api.gameprefs;

import java.util.HashMap;

public enum ELauncherVisibility {
    HIDE_REOPEN("hide launcher and re-open when game closes"), CLOSE("close launcher when game starts"), KEEP_OPEN("keep the launcher open");

    private static final HashMap<String, ELauncherVisibility> lookupMap = new HashMap<String, ELauncherVisibility>();

    private ELauncherVisibility(String lookupStr) {
        addLV(lookupStr);
    }

    private void addLV(String lookupStr) {
        lookupMap.put(lookupStr, this);
    }
}
