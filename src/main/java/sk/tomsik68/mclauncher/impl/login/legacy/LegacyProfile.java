package sk.tomsik68.mclauncher.impl.login.legacy;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.api.login.ISession;

@Deprecated
public final class LegacyProfile implements IProfile {

    private String pass;
    private String name;

    private final String SKINS_ROOT = "http://skins.minecraft.net/MinecraftSkins/";

    public LegacyProfile(String username, String password) {
        name = username;
        pass = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPassword() {
        return pass;
    }

    public boolean isRemember() {
        return pass.length() > 0;
    }

    @Override
    public String getSkinURL() {
        StringBuilder url = new StringBuilder(SKINS_ROOT);
        url = url.append(getName());
        url = url.append(".png");
        return url.toString();
    }

}
