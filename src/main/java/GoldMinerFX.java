import abstractions.boosts.AbstractBoost;
import abstractions.workers.AbstractWorker;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.text.DecimalFormat;


public class GoldMinerFX extends Application {

    public final Font WORKER_NAME_FONT = Font.font("Arial", 14.0);
    public GameManager game = new GameManager();
    private Label score;
    private final DecimalFormat doubleValueFormat = new DecimalFormat("0.00");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();

        try {
            game.boot();
            buildGui(root);
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        primaryStage.setTitle("GoldMiner");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        setupTimer();
    }

    private void tick(){
        game.harvest();
        game.expireBoosts();
        updateScore();
    }

    public void setupTimer(){
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void updateScore(){
        score.setText(doubleValueFormat.format(game.getHarvestedCoins()) + " uncji");
    }

    public void updateWorkerLabels(AbstractWorker worker, Label workerName, Label workerPrice){
        workerName.setText(worker.getName() + " (Poziom " + worker.getLevel() + ")");
        workerPrice.setText(doubleValueFormat.format(worker.getPrice()) + "$ na nast. poziom");
    }


    private void buildGui(GridPane root) {
        int row = 1;
        for(AbstractWorker worker : game.getWorkers())
            buildWorkerGui(root, worker, row++);

        buildGoldenNugget(root);
        buildScore(root);
    }

    private void buildScore(GridPane root) {
        score = new Label();
        score.setFont(WORKER_NAME_FONT);
        score.setTextAlignment(TextAlignment.RIGHT);
        updateScore();
        root.add(score, 0, 0);
    }

    private void buildGoldenNugget(GridPane root) {
        ImageView goldenNuggetIcon = new ImageView(new Image(game.GOLDEN_NUGGET_IMAGE_PATH));
        goldenNuggetIcon.setFitHeight(100);
        goldenNuggetIcon.setFitWidth(100);

        goldenNuggetIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            if (event.getButton() == MouseButton.PRIMARY) {
                game.harvestOnClick();
                AbstractBoost boost = game.tryApplyRandomBoost(null);
                if (boost != null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Bonus!");
                    alert.setHeaderText("Otrzymano bonus:\n" + boost.toString());

                    alert.show();
                }
                updateScore();
            }
        });
        root.add(goldenNuggetIcon, 5, game.getWorkers().size()/2);

    }

    private void buildWorkerGui(GridPane root, AbstractWorker worker, int row) {
        ImageView workerIcon = new ImageView(worker.getImage());
        workerIcon.setFitHeight(100);
        workerIcon.setFitWidth(100);
        root.add(workerIcon, 0, row);

        Label workerName = new Label(worker.getName() + " (Poziom " + worker.getLevel() + ")");
        workerName.setFont(WORKER_NAME_FONT);
        workerName.setTextAlignment(TextAlignment.RIGHT);
        root.add(workerName, 1, row);

        Label workerPrice = new Label(doubleValueFormat.format(worker.getPrice()) + "$ na nast. poziom");
        workerPrice.setFont(WORKER_NAME_FONT);
        root.add(workerPrice, 2, row);

        applyImageViewEvents(workerIcon, workerName, workerPrice, worker);
    }

    private void applyImageViewEvents(ImageView workerIcon, Label workerLevelText, Label workerPriceText, AbstractWorker worker) {
        workerIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kupno pracownika");

            if (game.upgrade(worker)) {
                alert.setHeaderText("Zakup " + worker.getName() + " zakończony sukcesem.");

                AbstractBoost boost = game.tryApplyRandomBoost(worker);
                if (boost != null)
                    alert.setContentText("Dodatkowo otrzymano bonus:\n" + boost.toString());

                updateWorkerLabels(worker, workerLevelText, workerPriceText);
                worker.playSound();
                updateScore();
            }
            else {
                alert.setHeaderText("Nie możesz sobie pozwolić na zakup " + worker.getName() + ".");
            }
            alert.show();
        });

        workerIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            workerIcon.setFitWidth(workerIcon.getFitWidth() + 5);
            workerIcon.setFitHeight(workerIcon.getFitHeight() + 5);
        });

        workerIcon.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            workerIcon.setFitWidth(workerIcon.getFitWidth() - 5);
            workerIcon.setFitHeight(workerIcon.getFitHeight() - 5);
        });
    }


}
