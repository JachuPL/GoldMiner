package windows;

import abstractions.boosts.AbstractBoost;
import abstractions.workers.AbstractWorker;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import services.GameManager;

import java.text.DecimalFormat;
import java.util.List;

public class WorkersController {
    private final Font WORKER_NAME_FONT = Font.font("Arial", 14.0);
    private final Font WORKER_LEVEL_FONT = Font.font("Arial", 24.0);
    private final DecimalFormat doubleValueFormat = new DecimalFormat("0.00");
    private final GameManager game = new GameManager();
    private final double SPLITTER_PCT_POS = 0.65;
    private final double MODIFIED_PIXELS_ON_INTERACT = 5;

    @FXML private Accordion workersAccordion;
    @FXML private SplitPane splitter;
    @FXML private AnchorPane workerAnchorPane;
    @FXML private ImageView goldenNuggetImage;
    @FXML private Label gatheredOunzes;
    @FXML private ImageView goldOunzesImage;

    private void lockSplitterOn(double percentage){
        workerAnchorPane.maxWidthProperty().bind(splitter.widthProperty().multiply(percentage));
        workerAnchorPane.minWidthProperty().bind(splitter.widthProperty().multiply(percentage));
    }

    private void buildWorkersGui(List<AbstractWorker> workers){
        lockSplitterOn(SPLITTER_PCT_POS);
        workersAccordion.prefWidthProperty().bind(splitter.widthProperty().multiply(0.65));
        for(AbstractWorker worker : workers)
            buildWorkerGui(worker);
    }

    private void buildWorkerGui(AbstractWorker worker){
        TitledPane workerPane = new TitledPane();
        workerPane.setText(worker.getName() + " (Poziom " + worker.getLevel() + ")");

        AnchorPane workerPaneBackgroundPane = new AnchorPane();
        workerPane.setContent(workerPaneBackgroundPane);

        ImageView workerIcon = new ImageView(worker.getImage());
        workerIcon.setFitHeight(100);
        workerIcon.setFitWidth(100);
        workerPaneBackgroundPane.getChildren().add(workerIcon);

        Label workerPrice = new Label(doubleValueFormat.format(worker.getPrice()) + "$");
        workerPrice.setFont(WORKER_NAME_FONT);
        workerPrice.setLayoutY(workerIcon.getFitHeight());
        workerPaneBackgroundPane.getChildren().add(workerPrice);

        Label workerLevel = new Label("Lv. " + worker.getLevel());
        workerLevel.setFont(WORKER_LEVEL_FONT);
        workerLevel.setLayoutX(workerIcon.getFitWidth() + MODIFIED_PIXELS_ON_INTERACT);
        workerLevel.setLayoutY(workerIcon.getFitHeight() / 2);
        workerPaneBackgroundPane.getChildren().add(workerLevel);

        workerIcon.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kupno pracownika");

            if (game.upgrade(worker)) {
                alert.setHeaderText("Zakup " + worker.getName() + " zakończony sukcesem.");

                AbstractBoost boost = game.tryApplyRandomBoost(worker);
                if (boost != null)
                    alert.setContentText("Dodatkowo otrzymano bonus:\n" + boost.toString());


                workerLevel.setText("Lv. " + worker.getLevel());
                workerPane.setText(worker.getName() + " (Poziom " + worker.getLevel() + ")");
                workerPrice.setText(worker.getPrice() + "$");
                worker.playSound();
                updateScore();
            }
            else {
                alert.setHeaderText("Nie możesz sobie pozwolić na zakup " + worker.getName() + ".");
            }
            alert.show();
        });

        workerIcon.addEventHandler(MouseEvent.MOUSE_ENTERED, mouseEvent -> {
            workerIcon.setFitWidth(workerIcon.getFitWidth() + MODIFIED_PIXELS_ON_INTERACT);
            workerIcon.setFitHeight(workerIcon.getFitHeight() + MODIFIED_PIXELS_ON_INTERACT);
        });

        workerIcon.addEventHandler(MouseEvent.MOUSE_EXITED, mouseEvent -> {
            workerIcon.setFitWidth(workerIcon.getFitWidth() - MODIFIED_PIXELS_ON_INTERACT);
            workerIcon.setFitHeight(workerIcon.getFitHeight() - MODIFIED_PIXELS_ON_INTERACT);
        });

        workersAccordion.getPanes().add(workerPane);
    }

    private void buildGoldenNugget() {
        goldenNuggetImage.setImage(new Image(game.GOLDEN_NUGGET_IMAGE_PATH));
        goldenNuggetImage.setFitHeight(150);
        goldenNuggetImage.setFitWidth(150);

        goldenNuggetImage.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
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

        goldOunzesImage.setImage(new Image(game.GOLDEN_NUGGET_IMAGE2_PATH));
    }

    private void updateScore(){
        gatheredOunzes.setText(doubleValueFormat.format(game.getHarvestedCoins()) + " uncji");
    }

    public void boot() throws Exception {
        game.boot();
        buildWorkersGui(game.getWorkers());
        buildGoldenNugget();
    }

    public void tick() {
        game.harvest();
        game.expireBoosts();
        updateScore();
    }
}
