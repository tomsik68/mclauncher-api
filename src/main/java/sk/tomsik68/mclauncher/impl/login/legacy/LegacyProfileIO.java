package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.common.MCLauncherAPI;
import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileIO;
import sk.tomsik68.mclauncher.util.FileUtils;

import java.io.*;

@Deprecated
public final class LegacyProfileIO implements IProfileIO {
    private final LegacyLoginEncryptionProcessor proc;
    private final File dest;

    public LegacyProfileIO(File mcInstance) {
        proc = new LegacyLoginEncryptionProcessor();
        dest = new File(mcInstance, "lastlogin");
    }

    @Override
    public IProfile[] read() throws Exception {
        String user, pass;
        DataInputStream input = new DataInputStream(proc.decrypt(new FileInputStream(dest)));
        user = input.readUTF();
        pass = input.readUTF();
        input.close();
        return new IProfile[]{new LegacyProfile(user, pass)};
    }

    @Override
    public void write(IProfile[] profile) throws Exception {
        // warn the developer if they want to save more profiles
        if(profile.length > 1){
            MCLauncherAPI.log.warning("Saving multiple profiles using LegacyProfileIO is not possible! MCLauncherAPI will only save the one that is 0th in the array. Other profiles won't be saved!");
        }
        // create the file if it doesn't exist
        if (!dest.exists()) {
            MCLauncherAPI.log.fine("lastlogin file doesn't exist. Creating it...");
            FileUtils.createFileSafely(dest);
            MCLauncherAPI.log.fine("lastlogin file created.");
        }
        // write the profile as 2 UTF strings encrypted with LegacyLoginEncryptionProcessor
        DataOutputStream out = new DataOutputStream(proc.encrypt(new FileOutputStream(dest)));
        out.writeUTF(profile[0].getName());
        out.writeUTF(profile[0].getPassword());
        out.flush();
        out.close();
    }

}
