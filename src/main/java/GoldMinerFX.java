import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.runners.model.InitializationError;
import services.BoostService;
import services.WorkerService;

public class GoldMinerFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        primaryStage.setTitle("GoldMiner");
        primaryStage.setScene(new Scene(root, 350, 350));
        primaryStage.show();

        try {
            InitializeSets();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void InitializeSets() throws Exception {
        WorkerService workerService = new WorkerService("workers.json");

        if (workerService.Load().size() == 0)
            throw new Exception("An error occured while reading workers.json");

        BoostService boostService = new BoostService("boosts.json");

        if(boostService.Load().size() == 0)
            throw new Exception("An error occured while reading boosts.json");
    }
}
