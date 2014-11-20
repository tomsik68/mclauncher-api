package sk.tomsik68.mclauncher.api.common;

/**
 * General interface for any Observable.
 * How this works is, that once observable changes, it notifies all {@link IObserver}s
 * about the change.
 *
 * @param <E> What will be changing
 * @author Tomsik68
 */
public interface IObservable<E> {
    /**
     * Adds specified observer to this observable.
     *
     * @param obs Observer to add
     */
    public void addObserver(IObserver<E> obs);

    /**
     * Deletes specified observer, so that it will not longer receive updates
     * from this observable.
     *
     * @param obs Observer to delete
     */
    public void deleteObserver(IObserver<E> obs);

    /**
     * Notifies observers about change of specified Object.
     *
     * @param changedObj Object that changed.
     */
    public void notifyObservers(E changedObj);
}
