package sk.tomsik68.mclauncher.impl.login.legacy;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

@Deprecated
final class LegacyLoginEncryptionProcessor {
    private static final int MODE_ENCRYPT = 1;
    private static final int MODE_DECRYPT = 2;
    private static final long SALT = 43287234L;
    private static final String PBE_KEY_STR = "passwordfile";

    // notchcode begin
    private static Cipher getCipher(int mode) throws Exception {
        Random random = new Random(SALT);
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, 5);

        SecretKey pbeKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec(PBE_KEY_STR.toCharArray())); //$NON-NLS-1$
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(mode, pbeKey, pbeParamSpec);
        return cipher;
    }
    // notchcode end

    InputStream decrypt(InputStream is) throws Exception {
        Cipher cipher = getCipher(MODE_DECRYPT);
        if (cipher == null) {
            throw new RuntimeException("Failed to create cipher for login stream decryption");
        } else
            return new CipherInputStream(is, cipher);
    }

    OutputStream encrypt(OutputStream os) throws Exception {
        Cipher cipher = getCipher(MODE_ENCRYPT);
        if (cipher == null) {
            throw new RuntimeException("Failed to create cipher for login stream encryption");
        }else
            return new CipherOutputStream(os, cipher);
    }

}
