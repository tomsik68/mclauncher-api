import static org.junit.Assert.*;

import org.junit.Test;

import sk.tomsik68.mclauncher.impl.common.Platform;


public class TestPlatform {

    @Test
    public void test() {
        assertTrue(Platform.getCurrentPlatform().equals(Platform.osByName("linux")));
    }

}
