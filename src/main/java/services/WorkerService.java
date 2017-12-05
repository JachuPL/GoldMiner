package services;

import abstractions.services.JSONLoaderService;
import abstractions.workers.AbstractWorker;
import model.Worker;
import org.json.simple.JSONObject;

public class WorkerService extends JSONLoaderService<AbstractWorker> {
    public WorkerService(String fileName) {
        super(fileName);
    }

    @Override
    protected void NodeProcessingCallback(Object item) {
        JSONObject object = (JSONObject)item;

        AbstractWorker worker = new Worker(
                Integer.parseInt(object.get("id").toString()),
                (String)object.get("name"),
                0,
                Double.parseDouble(object.get("baseCost").toString()),
                Double.parseDouble(object.get("unitsPerSec").toString())
        );
        _readEntities.add(worker);
    }
}
