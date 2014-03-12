package sk.tomsik68.mclauncher.impl.login.legacy.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;

public class LegacyProfileIO implements IProfileIO {
    private final LegacyLoginEncryptionProcessor proc;
    private final File dest;

    public LegacyProfileIO(File mcInstance) {
        proc = new LegacyLoginEncryptionProcessor();
        dest = new File(mcInstance,"lastlogin");
    }

    @Override
    public IProfile[] read() throws Exception {
        String user, pass;
        DataInputStream input = new DataInputStream(proc.decrypt(new FileInputStream(dest)));
        user = input.readUTF();
        pass = input.readUTF();
        input.close();
        return new IProfile[] { new LegacyProfile(user, pass) };
    }

    @Override
    public void write(IProfile[] profile) throws Exception {
        if(!dest.exists()){
            dest.getParentFile().mkdirs();
            dest.createNewFile();
        }
        DataOutputStream out = new DataOutputStream(proc.encrypt(new FileOutputStream(dest)));
        out.writeUTF(profile[0].getName());
        out.writeUTF(profile[0].getPassword());
        out.flush();
        out.close();
    }

}
