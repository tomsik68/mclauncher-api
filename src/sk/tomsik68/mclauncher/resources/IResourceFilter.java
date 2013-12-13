package sk.tomsik68.mclauncher.resources;

import java.util.Map;

public interface IResourceFilter {
    public boolean approves(Map<String, String> entries);
}
