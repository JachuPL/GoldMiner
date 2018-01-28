import abstractions.boosts.AbstractBoost;
import abstractions.boosts.BoostType;
import abstractions.workers.AbstractWorker;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import services.BoostService;
import services.WorkerService;

import java.io.File;
import java.util.List;

public class GameManager {
    public final String RESOURCES_BASE_PATH = "resources/";
    public final String CONFIG_BASE_PATH = RESOURCES_BASE_PATH + "conf/";
    public final String GOLDEN_NUGGET_IMAGE_PATH = "images/zloto.png";
    public final String MUSIC_BASE_PATH = RESOURCES_BASE_PATH + "sounds/";
    public final String BGM_PATH = MUSIC_BASE_PATH + "dark_rage.wav";

    private WorkerService workerService = null;
    private BoostService boostService = null;
    private List<AbstractWorker> workers = null;
    private List<AbstractBoost> boosts = null;
    private double harvestedCoins = 0;
    private MediaPlayer bgm;

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

    public double computePotentialHarvest() {
        return workers.stream().mapToDouble(f -> f.harvest()).sum();
    }

    public void harvest(){
        harvestedCoins += computePotentialHarvest();
    }

    public double computePotentialHarvestOnClick(){
        return workers.stream().mapToDouble(f -> f.harvestOnClick()).sum();
    }

    public void harvestOnClick() {
        harvestedCoins += computePotentialHarvestOnClick();
        harvestedCoins += 1;
    }

    public boolean upgrade(AbstractWorker worker){
        if (getHarvestedCoins() < worker.getPrice())
            return false;

        harvestedCoins -= worker.getPrice();
        worker.upgrade();

        return true;
    }

    public void expireBoosts(){ workers.stream().forEach(f -> f.expireBoosts()); }

    public void startPlayingMusic(){
            bgm = new MediaPlayer(new Media(new File(BGM_PATH).toURI().toString()));
            bgm.setOnEndOfMedia(() -> {
                bgm.seek(bgm.getStartTime());
                bgm.play();
            });
            bgm.play();
            bgm.seek(bgm.getStartTime());
    }

    public void boot() throws Exception {
        initializeSets();
        startPlayingMusic();
    }
}
