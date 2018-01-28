package windows;

import abstractions.workers.AbstractWorker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import services.GameManager;

import java.util.List;

public class WorkersController {
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
            workersAccordion.getPanes().add(new WorkerControl(worker));
    }

    public void boot() throws Exception {
        GameManager.instance().boot();
        buildWorkersGui(GameManager.instance().getWorkers());
        goldenNuggetControl.configure();
        goldenNuggetControl.updateScore();
    }

    public void tick() {
        GameManager.instance().harvest();
        GameManager.instance().expireBoosts();
        goldenNuggetControl.updateScore();
        for(TitledPane control : workersAccordion.getPanes())
        {
            try{
                WorkerControl control1 = (WorkerControl) control;
                control1.update();
            }
            catch(ClassCastException ex){
                ex.printStackTrace();
            }
        }
    }
}
