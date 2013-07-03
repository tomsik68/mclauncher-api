package sk.tomsik68.mclauncher.api.login;

import java.util.Map;

public interface ISession {
    public Object getParameter(String key);

    public Map<String, Object> asMap();
}
