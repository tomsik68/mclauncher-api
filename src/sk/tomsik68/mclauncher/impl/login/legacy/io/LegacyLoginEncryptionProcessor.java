package sk.tomsik68.mclauncher.impl.login.legacy.io;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class LegacyLoginEncryptionProcessor {
    private static final int MODE_ENCRYPT = 1;
    private static final int MODE_DECRYPT = 2;

    // notchcode
    private static Cipher getCipher(int mode) throws Exception {
        Random random = new Random(43287234L);
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);

        SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec("passwordfile".toCharArray())); //$NON-NLS-1$
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(mode, pbeKey, pbeParamSpec);
        return cipher;
    }

    public InputStream decrypt(InputStream is) throws Exception {
        Cipher cipher = getCipher(MODE_DECRYPT);
        if (cipher == null)
            return is;
        else
            return new CipherInputStream(is, cipher);
    }

    public OutputStream encrypt(OutputStream os) throws Exception {
        Cipher cipher = getCipher(MODE_ENCRYPT);
        if (cipher == null)
            return os;
        else
            return new CipherOutputStream(os, cipher);
    }

}
