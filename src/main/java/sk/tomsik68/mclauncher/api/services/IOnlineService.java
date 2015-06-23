package sk.tomsik68.mclauncher.api.services;

/**
 * Interface for all online services, mainly login
 *
 * @author Tomsik68
 */
public interface IOnlineService {
    /**
     * @param availability - Availability object retrieved from central server
     * @return True if this service is available. The result is to be determined using availability specified
     */
    public boolean isAvailable(IServicesAvailability availability);
}
