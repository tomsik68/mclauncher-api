package sk.tomsik68.mclauncher.api.login;

/**
 * ProfileIO can read and write profiles. One IO can contain more profiles.
 *
 * @author Tomsik68
 */
public interface IProfileIO {
    /**
     * Reads all profiles from this IO.
     *
     * @return All profiles from this IO
     * @throws Exception I/O errors
     */
    public IProfile[] read() throws Exception;

    /**
     * <b>Over-writes</b> all profiles in this IO by those specified in array below
     *
     * @param profiles New profiles
     * @throws Exception I/O errors
     */
    public void write(IProfile[] profiles) throws Exception;
}
