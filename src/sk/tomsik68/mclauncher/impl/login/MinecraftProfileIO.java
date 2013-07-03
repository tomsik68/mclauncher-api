package sk.tomsik68.mclauncher.impl.login;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import sk.tomsik68.mclauncher.api.login.IProfileEncryptionProcessor;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.impl.login.legacy.MinecraftLoginEncryptionProcessor;

public class MinecraftProfileIO implements IProfileIO {
    private IProfileEncryptionProcessor proc;

    public MinecraftProfileIO() {
        proc = new MinecraftLoginEncryptionProcessor();
    }

    @Override
    public IProfile read(InputStream in) throws Exception {
        String user, pass;
        DataInputStream input = new DataInputStream(proc.decrypt(in));
        user = input.readUTF();
        pass = input.readUTF();
        input.close();
        return new MinecraftProfile(user, pass);
    }

    @Override
    public void write(IProfile profile, OutputStream o) throws Exception {
        DataOutputStream out = new DataOutputStream(proc.encrypt(o));
        out.writeUTF(profile.getName());
        out.writeUTF(profile.getPassword());
        out.flush();
        out.close();
    }

    @Override
    public IProfileEncryptionProcessor getEncryptionProcessor() {
        return proc;
    }

    @Override
    public void setEncryptionProcessor(IProfileEncryptionProcessor proc) {
        this.proc = proc;
    }

}
