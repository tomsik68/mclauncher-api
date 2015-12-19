package sk.tomsik68.mclauncher.backend;

import sk.tomsik68.mclauncher.api.common.IObservable;
import sk.tomsik68.mclauncher.api.common.IObserver;

import java.util.ArrayList;
import java.util.List;

final class AddToListObserver implements IObserver<String> {
    private final List<String> list;
    AddToListObserver(){
        list = new ArrayList<String>();
    }
    @Override
    public void onUpdate(IObservable<String> observable, String changed) {
        list.add(changed);
    }

    public List<String> getList(){
        return list;
    }
}
