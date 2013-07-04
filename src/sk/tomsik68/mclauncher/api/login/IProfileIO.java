package sk.tomsik68.mclauncher.api.login;

public interface IProfileIO {
    public IProfile[] read() throws Exception;

    public void write(IProfile[] profile) throws Exception;
}
