package sk.tomsik68.mclauncher.api.login;

/**
 * Represents a profile - saved username & password
 *
 * @author Tomsik68
 */
public interface IProfile {
    /**
     * @return Username from this profile
     */
    public String getName();

    /**
     * @return Password from this profile
     */
    public String getPassword();

    /**
     * @return If launcher should remember password
     */
    public boolean isRemember();

    /**
     * Updates this profile with parameters from the new session
     *
     * @param session New session created from this profile
     */
    public void update(ISession session);
}
