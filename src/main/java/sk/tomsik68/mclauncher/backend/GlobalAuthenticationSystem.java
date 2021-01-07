package sk.tomsik68.mclauncher.backend;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.impl.common.Platform;
import sk.tomsik68.mclauncher.impl.login.legacy.LegacyProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDAuthProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDLoginService;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDProfileIO;

import java.io.File;
import java.io.FileNotFoundException;

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
     * @param profileName Name of profile you want to use. If there are more profiles available, you need to specify name of profile you want to use. If there is only one profile, API will use that profile automatically.
     * @return ISession which is necessary to play the game
     * @throws Exception File I/O, JSON parsing, Network I/O, Profile Selection errors
     */
    public static ISession login(String profileName) throws Exception {
        // initialise login service in the working directory
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        YDLoginService loginService = YDLoginService.mojang();
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
            if(profileName == null || profileName.length() == 0)
                throw new ProfileSelectionException();
            // try to find profile with specified name
            for (IProfile profile : profiles) {
                if (profile.getName().equals(profileName)) {
                    selectedProfile = profile;
                }
            }
        }
        // obtain session object from the profile
        ISession result = loginService.login(selectedProfile);
        // save the new authentication ID
        profileIO.write(profiles);
        return result;
    }

    /**
     * Performs password login with entered user details and saves session data to GlobalAuthenticationSystem
     * @param username
     * @param password
     * @return Login Session
     * @throws Exception
     */
    public static ISession doPasswordLogin(String username, String password) throws Exception {
        // initialise login service in the working directory
        File workingDirectory = Platform.getCurrentPlatform().getWorkingDirectory();
        YDLoginService loginService = YDLoginService.mojang();
        try {
            loginService.load(workingDirectory);
        } catch (FileNotFoundException ex) {
            loginService.save(workingDirectory);
        }

        // create legacy profile and login
        LegacyProfile loginProfile = new LegacyProfile(username, password);
        ISession result = loginService.login(loginProfile);

        // create (YD) profile from login data
        IProfile profile = loginService.createProfile(result);

        // save (YD) profile
        YDProfileIO profileIO = new YDProfileIO(workingDirectory);
        profileIO.write(new IProfile[]{ profile });

        // return the obtained session
        return result;

    }
}
