package sk.tomsik68.mclauncher.api.services;

public interface IService {
    public String getBranding();

    public boolean isAvailable(IServicesAvailability availability);
}
