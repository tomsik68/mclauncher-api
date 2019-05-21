package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

import java.util.*;

final class Argument {
    private final List<String> value;
    private final RuleList rules;

    private Argument(List<String> value, RuleList rules) {
        this.value = Collections.unmodifiableList(value);
        this.rules = rules;
    }

    static Argument fromJson(JSONObject json) {
        RuleList rules = RuleList.fromJson((JSONArray) json.get("rules"));
        Object value = json.get("value");
        List<String> values = new ArrayList<>();
        if (value instanceof JSONArray) {
            JSONArray arr = (JSONArray) value;
            for (Object s : arr) {
                values.add(s.toString());
            }
        } else if(value instanceof String) {
            values.add(value.toString());
        }
        return new Argument(values, rules);
    }

    public static Argument fromString(String s) {
        return new Argument(Arrays.asList(s), RuleList.empty());
    }

    List<String> getValue() {
        return value;
    }

    boolean applies(IOperatingSystem os, String version, IFeaturePredicate featurePredicate) {
        return rules.allows(os, version, featurePredicate);
    }
}
