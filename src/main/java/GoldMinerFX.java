import abstractions.workers.AbstractWorker;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GoldMinerFX extends Application {

    public final Font WORKER_NAME_FONT = Font.font("Arial", 14.0);
    public GameManager game = new GameManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        try {
            game.initializeSets();
            buildGui(root);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("GoldMiner");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }


    private void buildGui(GridPane root) {

        int row = 1;
        for(AbstractWorker worker : game.getWorkers())
            buildWorkerGui(root, worker, row++);

        buildGoldenNugget(root);


    }

    private void buildGoldenNugget(GridPane root) {
        ImageView goldenNuggetIcon = new ImageView(new Image(game.GOLDEN_NUGGET_IMAGE_PATH));
        goldenNuggetIcon.setFitHeight(100);
        goldenNuggetIcon.setFitWidth(100);

        goldenNuggetIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                game.harvestOnClick();
                System.out.println(game.getHarvestedCoins());
                // TODO: update label
            }
        });
        root.add(goldenNuggetIcon, 5, game.getWorkers().size()/2);

    }

    private void buildWorkerGui(GridPane root, AbstractWorker worker, int row) {
        ImageView workerIcon = new ImageView(worker.getImage());
        workerIcon.setFitHeight(100);
        workerIcon.setFitWidth(100);
        applyImageViewEvents(workerIcon, worker);
        root.add(workerIcon, 0, row);

        Label workerName = new Label(worker.getName() + " (Poziom " + worker.getLevel() + ")");
        workerName.setFont(WORKER_NAME_FONT);
        workerName.setTextAlignment(TextAlignment.RIGHT);
        root.add(workerName, 1, row);

        Label workerPrice = new Label(worker.getPrice() + "$ na nast. poziom");
        workerPrice.setFont(WORKER_NAME_FONT);
        root.add(workerPrice, 2, row);
    }

    private void applyImageViewEvents(ImageView workerIcon, AbstractWorker worker) {

        workerIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                event.consume();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Kupno pracownika");
                alert.setHeaderText("Nie możesz sobie pozwolić na zakup " + worker.getName() + ".");
                alert.setContentText(event.getButton().name().toString());
                alert.show();
            }
        });

        workerIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                workerIcon.setFitWidth(105);
                workerIcon.setFitHeight(105);
            }
        });

        workerIcon.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                workerIcon.setFitWidth(100);
                workerIcon.setFitHeight(100);
            }

        });
    }


}
