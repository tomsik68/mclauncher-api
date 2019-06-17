package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.junit.Before;
import org.junit.Test;

public final class TestRuleParser {

    @Test
    public void parseRuleWithFeatures() {
        String r = "{ \"action\": \"allow\", \"features\": { \"has_custom_resolution\": true } }";
        Rule rule = Rule.fromJson((JSONObject) JSONValue.parse(r));
    }

    @Test
    public void parseRuleWithOS() {
        String r = "{\n" + "            \"action\": \"allow\",\n" + "            \"os\": {\n" + "              \"name\": \"osx\"\n" + "            }\n" + "          }\n";
        Rule rule = Rule.fromJson((JSONObject) JSONValue.parse(r));
    }

    @Test
    public void parseRuleWithArch() {
        String r = "{ \"action\": \"allow\", \"os\": { \"arch\": \"x86\" } }";
        Rule rule = Rule.fromJson((JSONObject) JSONValue.parse(r));
    }
}
