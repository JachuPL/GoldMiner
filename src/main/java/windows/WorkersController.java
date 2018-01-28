package windows;

import abstractions.workers.AbstractWorker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import services.GameManager;

import java.util.List;

public class WorkersController {
    private final GameManager game = new GameManager();
    private final double SPLITTER_PCT_POS = 0.65;

    @FXML private Accordion workersAccordion;
    @FXML private SplitPane splitter;
    @FXML private AnchorPane workerAnchorPane;
    @FXML private GoldenNuggetControl goldenNuggetControl;

    private void lockSplitterOn(double percentage){
        workerAnchorPane.maxWidthProperty().bind(splitter.widthProperty().multiply(percentage));
        workerAnchorPane.minWidthProperty().bind(splitter.widthProperty().multiply(percentage));
    }

    private void buildWorkersGui(List<AbstractWorker> workers){
        lockSplitterOn(SPLITTER_PCT_POS);
        workersAccordion.prefWidthProperty().bind(splitter.widthProperty().multiply(0.65));

        for(AbstractWorker worker : workers)
            workersAccordion.getPanes().add(new WorkerControl(worker, game));
    }

    public void boot() throws Exception {
        game.boot();
        buildWorkersGui(game.getWorkers());
        goldenNuggetControl.configure(game);
        goldenNuggetControl.updateScore();
    }

    public void tick() {
        game.harvest();
        game.expireBoosts();
        goldenNuggetControl.updateScore();
    }
}
