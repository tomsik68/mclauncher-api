package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.Test;

public final class TestArgumentParser {
    @Test
    public void parseArgument() {
        JSONObject json = (JSONObject) JSONValue.parse( "{ \"rules\": [ { \"action\": \"allow\", \"os\": { \"arch\": \"x86\" } } ], \"value\": \"-Xss1M\" }");
        Argument.fromJson(json);
    }
}
