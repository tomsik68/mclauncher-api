package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.*;

final class ArgumentList implements Iterable<Argument> {
    private static final ArgumentList EMPTY = new ArgumentList(Collections.<Argument>emptyList());
    private final List<Argument> args;

    private ArgumentList(List<Argument> arguments) {
        this.args = Collections.unmodifiableList(arguments);
    }

    static ArgumentList fromString(String s) {
        List<String> tokens = Arrays.asList(s.split(" "));
        List<Argument> args = new ArrayList<>(tokens.size());
        for (String tok : tokens) {
            args.add(Argument.fromString(tok));
        }
        return new ArgumentList(args);
    }

    static ArgumentList fromArray(JSONArray array) {
        List<Argument> arguments = new ArrayList<>();
        for (Object a : array) {
            if (a instanceof String)
                arguments.add(Argument.fromString((String) a));
            else if(a instanceof JSONObject)
                arguments.add(Argument.fromJson((JSONObject) a));
        }
        return new ArgumentList(arguments);
    }

    public static ArgumentList empty() {
        return EMPTY;
    }

    boolean isEmpty() {
        return args.isEmpty();
    }

    @Override
    public Iterator<Argument> iterator() {
        return args.iterator();
    }
}
