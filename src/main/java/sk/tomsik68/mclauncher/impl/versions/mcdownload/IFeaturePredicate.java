package sk.tomsik68.mclauncher.impl.versions.mcdownload;

interface IFeaturePredicate {
    boolean isFeatureSatisfied(String key, boolean value);
}
