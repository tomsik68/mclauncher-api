package sk.tomsik68.mclauncher.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import sk.tomsik68.mclauncher.api.login.IProfile;
import sk.tomsik68.mclauncher.impl.login.yggdrasil.YDAuthProfile;

public class SkinRetriever {
    public SkinRetriever() {

    }

    public BufferedImage getSkin(String username) throws MalformedURLException, IOException {
        String url = String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", username);
        return ImageIO.read(new URL(url));
    }

    public BufferedImage getSkin(IProfile profile) throws MalformedURLException, IOException {
        String username = profile.getName();
        if (profile instanceof YDAuthProfile) {
            username = ((YDAuthProfile) profile).getUuid();
        }
        return getSkin(username);
    }
}
