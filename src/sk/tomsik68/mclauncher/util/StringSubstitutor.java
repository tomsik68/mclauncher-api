package sk.tomsik68.mclauncher.util;

import java.util.HashMap;
import java.util.Map.Entry;

public class StringSubstitutor {
    private HashMap<String, String> variables = new HashMap<String, String>();
    private final String template;

    public StringSubstitutor(String tmp) {
        template = tmp;
    }

    public String substitute(String s) {
        for (Entry<String, String> variable : variables.entrySet()) {
                s = s.replace(String.format(template,variable.getKey()), variable.getValue());
        }
        return s;
    }

    public void setVariable(String key, String val) {
        variables.put(key, val);
    }
}
