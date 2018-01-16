package services;

import abstractions.services.JSONLoaderService;
import abstractions.workers.AbstractWorker;
import javafx.scene.image.Image;
import model.Worker;
import org.json.simple.JSONObject;

public class WorkerService extends JSONLoaderService<AbstractWorker> {
    public WorkerService(String fileName) {
        super(fileName);
    }

    @Override
    protected void NodeProcessingCallback(Object item) {
        JSONObject object = (JSONObject)item;


        Image icon = new Image(object.get("icon").toString());

        AbstractWorker worker = new Worker(
                Integer.parseInt(object.get("id").toString()),
                object.get("name").toString(),
                0,
                Double.parseDouble(object.get("baseCost").toString()),
                Double.parseDouble(object.get("unitsPerSec").toString()),
                icon
        );
        _readEntities.add(worker);
    }
}
