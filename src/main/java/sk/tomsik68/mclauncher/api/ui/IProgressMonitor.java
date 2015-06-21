package sk.tomsik68.mclauncher.api.ui;

/**
 * ProgressMonitors receive updates about different parts of MCLauncherAPI
 * progress. It can be anything from a simple CLI up to dialog with a progress
 * bar.
 *
 * @author Tomsik68
 */
public interface IProgressMonitor {
    /**
     * Sets current progress
     *
     * @param progress New progress value
     */
    public void setProgress(int progress);

    /**
     * Sets maximum progress value
     *
     * @param len Max progress value
     */
    public void setMax(int len);

    /**
     * Increments current progress value by <code>amount</code>
     *
     * @param amount
     */
    public void incrementProgress(int amount);

}
