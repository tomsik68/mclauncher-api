package sk.tomsik68.mclauncher.api.common;

/**
 * How this works is, that once {@link IObservable} changes, it notifies all observers
 * about the change.
 *
 * @param <E> Watched object type
 * @author Tomsik68
 */
public interface IObserver<E> {
    /**
     * This method indicates, that an object in an observable has updated.
     *
     * @param observable The source
     * @param changed    Changed object
     */
    public void onUpdate(IObservable<E> observable, E changed);
}
