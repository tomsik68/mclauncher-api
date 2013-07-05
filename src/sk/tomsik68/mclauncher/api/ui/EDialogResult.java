package sk.tomsik68.mclauncher.api.ui;

import javax.swing.JOptionPane;

public enum EDialogResult {
    YES, NO, CANCEL, OK;

    public static EDialogResult fromJOptionPane(int result) {
        if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.OK_CANCEL_OPTION)
            return OK;
        else if (result == JOptionPane.YES_OPTION)
            return YES;
        else if (result == JOptionPane.NO_OPTION)
            return NO;
        return CANCEL;
    }
}
