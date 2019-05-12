package sk.tomsik68.mclauncher.impl.versions.mcdownload;

enum FeaturePreds implements IFeaturePredicate {
    ALL {
        @Override
        public boolean isFeatureSatisfied(String key, boolean value) {
            return true;
        }
    },
    NONE {
        @Override
        public boolean isFeatureSatisfied(String key, boolean value) {
            return false;
        }
    }


}
