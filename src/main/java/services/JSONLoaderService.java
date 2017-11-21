package services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public abstract class JSONLoaderService<T> extends DataLoaderService<T>
{
    protected String _fileName;
    protected List<T> _readEntities;
    public JSONLoaderService(String fileName) {
        _fileName = fileName;
        _readEntities = new ArrayList<T>();
    }

    public List<T> Load()
    {
        JSONParser parser = new JSONParser();
        try
        {
            Object obj = parser.parse(new FileReader(_fileName));
            JSONArray jsonArray = (JSONArray) obj;
            for (Object item : jsonArray)
                NodeProcessingCallback(item);


        }
        catch(Exception ex)
        {

        }

        return _readEntities;
    }
}
