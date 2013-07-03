package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.ISession;
import sk.tomsik68.mclauncher.api.login.ISessionFactory;

public class LegacySessionFactory implements ISessionFactory {

    @Override
    public ISession createSession(String[] params) throws Exception {
        if (params == null || params.length <= 1)
            throw new IllegalArgumentException("Unable to login: "+params[0]);
        for(String p : params){
            System.out.println(p);
        }
        return new LegacySession(params[2], params[3], params[4], params[1], params[0]);
    }

}
