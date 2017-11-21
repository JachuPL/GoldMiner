package services;

import java.util.List;

public abstract class DataLoaderService<T> {

    protected abstract void NodeProcessingCallback(Object item);

    public abstract List<T> Load();

}
