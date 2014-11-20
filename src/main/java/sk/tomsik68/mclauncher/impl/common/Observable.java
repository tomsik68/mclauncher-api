package sk.tomsik68.mclauncher.impl.common;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;

import java.util.HashSet;

public class Observable<E> implements IObservable<E> {
    private final HashSet<IObserver<E>> observers = new HashSet<IObserver<E>>();

    @Override
    public void addObserver(IObserver<E> obs) {
        observers.add(obs);
    }

    @Override
    public void deleteObserver(IObserver<E> obs) {
        observers.remove(obs);
    }

    @Override
    public void notifyObservers(E changedObj) {
        for (IObserver<E> obs : observers) {
            obs.onUpdate(this, changedObj);
        }
    }

}
