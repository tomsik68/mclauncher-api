package sk.tomsik68.mclauncher.api.gameprefs;

import java.util.HashMap;

/**
 * This is used for compatibility with the official launcher.
 * Launcher visibility rules determine how the launcher behaves when you start game.
 *
 * @author Tomsik68
 */
public enum ELauncherVisibility {
    /**
     * Launcher hides when you start the game and re-opens when game closes.
     */
    HIDE_REOPEN("hide launcher and re-open when game closes"),

    /**
     * Launcher is closed(exited, killed, whatever) when game is started and game is left alone.
     */
    CLOSE("close launcher when game starts"),

    /**
     * Launcher is open all the time
     */
    KEEP_OPEN("keep the launcher open");

    private static final HashMap<String, ELauncherVisibility> lookupMap = new HashMap<String, ELauncherVisibility>();

    private ELauncherVisibility(String lookupStr) {
        addLV(lookupStr);
    }

    /** addLookupValue :) */
    private void addLV(String lookupStr) {
        lookupMap.put(lookupStr, this);
    }
}
