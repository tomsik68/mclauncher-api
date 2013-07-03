package sk.tomsik68.mclauncher.api.common;

public interface IObservable<E> {
    public void addObserver(IObserver<E> obs);

    public void deleteObserver(IObserver<E> obs);

    public void notifyObservers(E changedObj);
}
