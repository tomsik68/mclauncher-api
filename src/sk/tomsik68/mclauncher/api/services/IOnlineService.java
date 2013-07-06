package sk.tomsik68.mclauncher.api.services;

public interface IOnlineService extends IService {
    public boolean isAvailable(IServicesAvailability available);
}
