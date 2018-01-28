import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import windows.WorkersController;

import java.io.IOException;


public class GoldMinerFX extends Application {

    private WorkersController controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("windows/MainWindow.fxml"));

        VBox root = loader.load();

        try {
            controller = loader.getController();
            controller.boot();
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("GoldMiner");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        setupTimer();
    }


    private void tick(){
        controller.tick();
    }

    private void setupTimer(){
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
}
