package sk.tomsik68.mclauncher.api.login;

import java.io.InputStream;
import java.io.OutputStream;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.ISearchable;

public interface IProfileCache extends IObservable<IProfile>, ISearchable {
    public int getProfileCount();

    public InputStream getProfileInputStream(int index);

    public OutputStream getProfileOutputStream(int index);
}
