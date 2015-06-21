package sk.tomsik68.mclauncher.backend;

import sk.tomsik68.mclauncher.api.common.ILaunchSettings;

import java.io.File;
import java.util.List;
import java.util.Map;

final class DefaultLaunchSettings implements ILaunchSettings {
    @Override
    public String getInitHeap() {
        return "256M";
    }

    @Override
    public String getHeap() {
        return "1G";
    }

    @Override
    public Map<String, String> getCustomParameters() {
        return null;
    }

    @Override
    public List<String> getJavaArguments() {
        return null;
    }

    @Override
    public List<String> getCommandPrefix() {
        return null;
    }

    @Override
    public File getJavaLocation() {
        return null;
    }

    @Override
    public boolean isModifyAppletOptions() {
        return false;
    }
}
