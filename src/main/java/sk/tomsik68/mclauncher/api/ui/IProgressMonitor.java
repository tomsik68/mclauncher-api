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
     * Sets current progress. <B>Please note this value is between 0 and max_progress.
     * You need to scale it yourself in order to get progress information between 0 and 100.</B>
     *
     * @param progress New progress value
     * @see IProgressMonitor#setMax(int)
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
     * @param amount How much
     */
    public void incrementProgress(int amount);

}
