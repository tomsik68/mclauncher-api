package sk.tomsik68.mclauncher.api.services;

/**
 * Information about online services availability
 *
 * @author Tomsik68
 */
public interface IServicesAvailability {
    /**
     * @param name Name of the service
     * @return True if specified service is available
     */
    public boolean isServiceAvailable(String name);
}
