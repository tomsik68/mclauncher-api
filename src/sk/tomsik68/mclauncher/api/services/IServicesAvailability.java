package sk.tomsik68.mclauncher.api.services;

import sk.tomsik68.mclauncher.api.common.ISearchable;

public interface IServicesAvailability extends ISearchable {
    public boolean isServiceAvailable(String name);
}
