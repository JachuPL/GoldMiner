import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
    }
}
