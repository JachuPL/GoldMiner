package services;

import java.util.ArrayList;
import java.util.List;

public abstract class XMLLoaderService<T> extends DataLoaderService<T> {
    protected String _fileName;
    protected List<T> _readEntities;

    public XMLLoaderService(String fileName)
    {
        _fileName = fileName;
        _readEntities = new ArrayList<T>();
    }

    
}
