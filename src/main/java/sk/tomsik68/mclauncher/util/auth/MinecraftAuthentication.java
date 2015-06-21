package sk.tomsik68.mclauncher.util.auth;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;

import java.io.File;

public final class MinecraftAuthentication {

    public static ISession login(String name) throws Exception {
        // initialise login service in the working directory
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        YDLoginService loginService = new YDLoginService();
        loginService.load(workingDirectory);

        // read profiles
        YDProfileIO profileIO = new YDProfileIO(workingDirectory);
        IProfile[] profiles = profileIO.read();
        // select first profile.
        IProfile selectedProfile = profiles[0];
        // if there are more profiles and name is not empty
        if(profiles.length > 1 && name != null && name.length() > 0) {
            // try to find profile with specified name
            for (IProfile profile : profiles) {
                if (profile.getName().equals(name)) {
                    selectedProfile = profile;
                }
            }
        }
        // obtain session object from the profile
        ISession result = loginService.login(selectedProfile);
        // update the profile and write out the new authentication ID
        selectedProfile.update(result);
        profileIO.write(profiles);
        return result;
    }

}
