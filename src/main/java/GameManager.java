import abstractions.boosts.AbstractBoost;
import abstractions.boosts.BoostType;
import abstractions.workers.AbstractWorker;
import services.BoostService;
import services.WorkerService;

import java.util.List;

public class GameManager {
    public final String RESOURCES_BASE_PATH = "resources/";
    public final String CONFIG_BASE_PATH = RESOURCES_BASE_PATH + "conf/";
    public final String GOLDEN_NUGGET_IMAGE_PATH = "images/zloto.png";

    private WorkerService workerService = null;
    private BoostService boostService = null;
    private List<AbstractWorker> workers = null;
    private List<AbstractBoost> boosts = null;
    private double harvestedCoins = 0;

    public List<AbstractWorker> getWorkers() { return workers; }
    public List<AbstractBoost> getBoosts() { return boosts; }
    public double getHarvestedCoins() { return harvestedCoins; }

    public void initializeSets() throws Exception {
        workerService = new WorkerService(CONFIG_BASE_PATH + "workers.json");
        workers = workerService.Load();
        if (workers.size() == 0)
            throw new Exception("An error occured while reading workers.json");

        boostService = new BoostService(CONFIG_BASE_PATH + "boosts.json");
        boosts = boostService.Load();
        if(boosts.size() == 0)
            throw new Exception("An error occured while reading boosts.json");
    }

    public void harvest(){
        harvestedCoins += workers.stream().mapToDouble(f -> f.harvest()).sum();
    }

    public void harvestOnClick() {
        harvestedCoins += workers.stream().mapToDouble(f -> f.harvestOnClick()).sum();
        harvestedCoins += 1;
    }

}
