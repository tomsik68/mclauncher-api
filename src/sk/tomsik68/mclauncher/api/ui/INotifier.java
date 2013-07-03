package sk.tomsik68.mclauncher.api.ui;


public interface INotifier {
    public EDialogResult yesNoCancel(String message);

    public EDialogResult message(String message);

    public EDialogResult yesNo(String message);

    public IProgressMonitor displayProgressDialog(String message);

    public void error(Exception e);

}
