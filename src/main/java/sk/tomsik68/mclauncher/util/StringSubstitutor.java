package sk.tomsik68.mclauncher.util;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Replaces certain parts of string
 *
 */
public final class StringSubstitutor {
    private final String template;
    private HashMap<String, String> variables = new HashMap<String, String>();

    /**
     * Creates a StringSubstitutor that replaces variable in a way like you specify in template.
     * @param tmp - for example <code>${%s}</code> will substitute %s for key and then replace it by value
     */
    public StringSubstitutor(String tmp) {
        template = tmp;
    }

    public String substitute(String s) {
        for (Entry<String, String> variable : variables.entrySet()) {
            s = s.replace(variable.getKey(), variable.getValue());
        }
        return s;
    }

    public void setVariable(String key, String val) {
        variables.put(String.format(template, key), val);
    }
}
