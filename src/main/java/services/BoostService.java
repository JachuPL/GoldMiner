package services;

import abstractions.boosts.*;
import abstractions.services.JSONLoaderService;
import model.Boost;
import org.json.simple.JSONObject;

public class BoostService extends JSONLoaderService<AbstractBoost> {
    BoostService(String fileName) {
        super(fileName);
    }

    @Override
    protected void NodeProcessingCallback(Object item) {
        JSONObject object = (JSONObject) item;

        AbstractBoost boost = new Boost(
                Integer.parseInt(object.get("id").toString()),
                BoostCategory.valueOf(object.get("category").toString()),
                BoostType.valueOf(object.get("type").toString()),
                Double.parseDouble(object.get("value").toString()),
                Long.parseLong(object.get("duration").toString())
        );
        _readEntities.add(boost);
    }
}
