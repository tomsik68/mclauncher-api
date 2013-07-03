package sk.tomsik68.mclauncher.api.ui;

public interface IProgressMonitor {

    public void setProgress(int progress);

    public void setMax(int len);
    
    public void incrementProgress(int amount);
    
    public void finish();
}
