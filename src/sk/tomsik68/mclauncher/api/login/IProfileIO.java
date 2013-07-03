package sk.tomsik68.mclauncher.api.login;

import java.io.InputStream;
import java.io.OutputStream;

public interface IProfileIO {
    public IProfile read(InputStream input) throws Exception;

    public void write(IProfile profile, OutputStream out) throws Exception;

    public IProfileEncryptionProcessor getEncryptionProcessor();

    public void setEncryptionProcessor(IProfileEncryptionProcessor proc);
}
