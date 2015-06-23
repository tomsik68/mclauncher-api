package sk.tomsik68.mclauncher.api.login;

/**
 * Represents a profile - saved username and password
 *
 * @author Tomsik68
 */
public interface IProfile {
    /**
     * @return Username from this profile
     */
    public String getName();

    /**
     *
     * @return URL of this player's skin
     */
    public String getSkinURL();

    /**
     * @return Password from this profile
     */
    public String getPassword();

}
