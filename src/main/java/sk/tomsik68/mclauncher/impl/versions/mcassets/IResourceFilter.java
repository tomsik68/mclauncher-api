package sk.tomsik68.mclauncher.impl.versions.mcassets;

import java.util.Map;

public interface IResourceFilter {
    public boolean approves(Map<String, String> entries);
}
