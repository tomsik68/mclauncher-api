package sk.tomsik68.mclauncher.api.common;

public interface IObserver<E> {
    public void onUpdate(IObservable<E> observable, E changed);
}
