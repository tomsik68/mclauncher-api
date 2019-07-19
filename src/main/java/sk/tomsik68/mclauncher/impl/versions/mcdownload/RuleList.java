package sk.tomsik68.mclauncher.impl.versions.mcdownload;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.common.IOperatingSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class RuleList {
    private final List<Rule> rules;
    private static final RuleList EMPTY = new RuleList(Collections.<Rule>emptyList());

    private RuleList(List<Rule> rules) {
        Objects.requireNonNull(rules);
        this.rules = Collections.unmodifiableList(rules);
    }

    static RuleList fromJson(JSONArray rulesArray) {
        if (rulesArray == null || rulesArray.isEmpty())
            return empty();

        List<Rule> rules = new ArrayList<>();
        for (Object r : rulesArray) {
            rules.add(Rule.fromJson((JSONObject) r));
        }

        return new RuleList(rules);
    }

    boolean allows(IOperatingSystem os, String version, IFeaturePredicate featurePred) {
        Objects.requireNonNull(featurePred);

        if (rules.isEmpty()) {
            return true;
        }

        boolean isAllowed = false;

        for (Rule rule : rules) {
            if (rule.applies(featurePred)) {
                isAllowed = (rule.getAction() == Rule.Action.ALLOW);
            }
        }

        return isAllowed;

    }

    static RuleList empty() {
        return EMPTY;
    }

    RuleList and(RuleList other) {
        List<Rule> newRules = new ArrayList<>(this.rules);
        newRules.addAll(other.rules);
        return new RuleList(newRules);
    }
}
