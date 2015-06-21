package sk.tomsik68.mclauncher.backend;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;

import java.io.File;

/**
 * Global(system-wide) authentication system.
 * It loads authentication information from .minecraft directory and updates them after login,
 * so you'll never need to enter your password into a custom launcher.
 */
public final class GlobalAuthenticationSystem {

    /**
     * Obtains list of profiles <b>in .minecraft directory</b>
     * @return List of names of profiles in .minecraft directory
     * @throws Exception - File I/O errors, JSON parsing errors
     */
    public static String[] getProfileNames() throws Exception {
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        YDProfileIO profileIO = new YDProfileIO(workingDirectory);
        IProfile[] profiles = profileIO.read();
        String[] profileNames = new String[profiles.length];
        for(int i = 0; i < profiles.length; ++i){
            profileNames[i] = profiles[i].getName();
        }
        return profileNames;
    }

    /**
     * Tries to perform login using profile with specified name
     * @param name Name of profile you want to use. If there are
     * @return ISession which is necessary to play the game
     * @throws Exception File I/O, JSON parsing, Network I/O, Profile Selection errors
     */
    public static ISession login(String name) throws Exception {
        // initialise login service in the working directory
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        YDLoginService loginService = new YDLoginService();
        loginService.load(workingDirectory);

        // read profiles
        YDProfileIO profileIO = new YDProfileIO(workingDirectory);
        IProfile[] profiles = profileIO.read();
        if(profiles.length == 0)
            throw new ProfileSelectionException("There are no profiles in .minecraft directory!");
        // select first profile.
        IProfile selectedProfile = profiles[0];
        // if there are more profiles and name is not empty
        if(profiles.length > 1) {
            selectedProfile = null;
            if(name == null || name.length() == 0)
                throw new ProfileSelectionException();
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
