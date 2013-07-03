package sk.tomsik68.mclauncher.api.login;

import java.io.InputStream;
import java.io.OutputStream;

public interface ILoginEncryptionProcessor {
    public InputStream decrypt(InputStream is) throws Exception;

    public OutputStream encrypt(OutputStream os) throws Exception;
}
