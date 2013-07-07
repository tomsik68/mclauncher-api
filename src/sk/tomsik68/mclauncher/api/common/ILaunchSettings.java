package sk.tomsik68.mclauncher.api.common;

import java.util.List;
import java.util.Map;

public interface ILaunchSettings {
    /**
     * 
     * @return how much RAM to allocate (-Xms argument)
     */
    public String getInitHeap();

    /**
     * 
     * @return how much RAM to allocate (-Xmx argument)
     */
    public String getHeap();

    public String getWorkingDirectory();

    /**
     * 
     * @return Map of custom parameters for either minecraft applet or minecraft
     *         main method(depends on version). May be null.
     */
    public Map<String, String> getCustomParameters();

    /**
     * 
     * @return Whether to redirect process error stream to process input stream.
     *         If you're unsure, set to true.
     */
    public boolean isErrorStreamRedirected();

    /**
     * 
     * @return command list to append before the minecraft launch command. Can
     *         be glc-capture or other programs that need process pointer...
     */
    public List<String> getCommandPrefix();

    /**
     * 
     * @return If applet should open a table with options to change. False if
     *         unsure.
     */
    public boolean isModifyAppletOptions();
}
