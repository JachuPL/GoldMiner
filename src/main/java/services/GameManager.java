package services;

import abstractions.boosts.AbstractBoost;
import abstractions.workers.AbstractWorker;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.List;
import java.util.Random;

public class GameManager {
    private static GameManager game = new GameManager();

    private GameManager(){ }

    public static GameManager instance() { return game; }

    private final String RESOURCES_BASE_PATH = "resources/";
    private final String CONFIG_BASE_PATH = RESOURCES_BASE_PATH + "conf/";
    public final String GOLDEN_NUGGET_IMAGE_PATH = "images/zloto.png";
    public final String GOLDEN_NUGGET_IMAGE2_PATH = "images/zloto2.png";
    private final String MUSIC_BASE_PATH = RESOURCES_BASE_PATH + "sounds/";
    private final String BGM_PATH = MUSIC_BASE_PATH + "dark_rage.wav";

    private WorkerService workerService = null;
    private BoostService boostService = null;
    private List<AbstractWorker> workers = null;
    private List<AbstractBoost> boosts = null;
    private double harvestedCoins = 0;
    private MediaPlayer bgm;
    private Random randomGenerator = new Random();

    public List<AbstractWorker> getWorkers() { return workers; }
    public double getHarvestedCoins() { return harvestedCoins; }

    private void initializeSets() throws Exception {
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
        return workers.stream().mapToDouble(AbstractWorker::harvest).sum();
    }

    public void harvest(){
        harvestedCoins += computePotentialHarvest();
    }

    public double computePotentialHarvestOnClick(){
        return workers.stream().mapToDouble(AbstractWorker::harvestOnClick).sum() + 1;
    }

    public void harvestOnClick() {
        harvestedCoins += computePotentialHarvestOnClick();
    }

    public boolean upgrade(AbstractWorker worker){
        if (getHarvestedCoins() < worker.getPrice())
            return false;

        harvestedCoins -= worker.getPrice();
        worker.upgrade();

        return true;
    }

    public void expireBoosts(){ workers.forEach(AbstractWorker::expireBoosts); }

    private void startPlayingMusic(){
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

    public AbstractBoost tryApplyRandomBoost(AbstractWorker worker) {
        int baseChance = 5;     // default chance is 5%
        int maxChance = 100;

        if (worker == null) {
            maxChance *= 10;    // decrease chance if applying by click
            baseChance *= 2;    // make it at least 1%
            int workerIndex = randomGenerator.nextInt(workers.size());
            worker = workers.get(workerIndex);
            if (worker.getLevel() == 0)
                return null;
        }

        AbstractBoost boost = null;

        if (baseChance >= randomGenerator.nextInt(maxChance))
        {
            int boostIndex = randomGenerator.nextInt(boosts.size());
            boost = boosts.get(boostIndex);

            try {
                boost = boost.clone();
                worker.addBoost(boost);
            }
            catch(IllegalArgumentException ex){
                tryApplyRandomBoost(worker);
            }
        }

        return boost;
    }
}
