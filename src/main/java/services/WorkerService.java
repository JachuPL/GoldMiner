package services;

import abstractions.services.JSONLoaderService;
import abstractions.workers.AbstractWorker;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import model.Worker;
import org.json.simple.JSONObject;

import java.io.File;

public class WorkerService extends JSONLoaderService<AbstractWorker> {
    WorkerService(String fileName) {
        super(fileName);
    }

    @Override
    protected void NodeProcessingCallback(Object item) {
        JSONObject object = (JSONObject)item;


        Image icon = new Image(object.get("icon").toString());
        Media sound = new Media(new File(object.get("sound").toString()).toURI().toString());

        AbstractWorker worker = new Worker(
                Integer.parseInt(object.get("id").toString()),
                object.get("name").toString(),
                0,
                Double.parseDouble(object.get("baseCost").toString()),
                Double.parseDouble(object.get("unitsPerSec").toString()),
                icon,
                sound
        );
        _readEntities.add(worker);
    }
}
