package sk.tomsik68.mclauncher.impl.login;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.IProfileCache;
import sk.tomsik68.mclauncher.impl.common.Observable;

public class MultipleProfileCaches extends Observable<IProfile> implements IProfileCache {
    private final ArrayList<IProfileCache> activeCaches = new ArrayList<IProfileCache>();

    public MultipleProfileCaches(IProfileCache... caches) {
        for (IProfileCache cache : caches) {
            activeCaches.add(cache);
        }
    }

    @Override
    public int getProfileCount() {
        int result = 0;
        for (IProfileCache cache : activeCaches) {
            result += cache.getProfileCount();
        }
        return result;
    }

    @Override
    public InputStream getProfileInputStream(int index) {
        for (IProfileCache cache : activeCaches) {
            int profilesInCache = cache.getProfileCount();
            if (index < profilesInCache) {
                return cache.getProfileInputStream(index);
            }
            index -= (profilesInCache + 1);
        }

        return null;
    }

    @Override
    public OutputStream getProfileOutputStream(int index) {
        for (IProfileCache cache : activeCaches) {
            int profilesInCache = cache.getProfileCount();
            if (index < profilesInCache) {
                return cache.getProfileOutputStream(index);
            }
            index -= (profilesInCache + 1);
        }
        return null;
    }

}
