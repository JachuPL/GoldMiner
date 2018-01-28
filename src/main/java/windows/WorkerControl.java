package windows;

import abstractions.boosts.AbstractBoost;
import abstractions.workers.AbstractWorker;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import services.GameManager;

import java.io.IOException;
import java.text.DecimalFormat;

public class WorkerControl extends TitledPane {
    private static final Font WORKER_PRICE_FONT = Font.font("Arial", 16);
    private static final Font WORKER_LEVEL_FONT = Font.font("Arial", 24);
    private static final double MODIFIED_PIXELS_ON_INTERACT = 5;
    private static final DecimalFormat doubleValueFormat = new DecimalFormat("0.00");

    @FXML private Label workerLevel;
    @FXML private Label workerPrice;
    @FXML private ImageView workerImage;
    @FXML private AnchorPane workerAnchorPane;

    private AbstractWorker worker;
    private GameManager game;

    public WorkerControl(AbstractWorker worker, GameManager game){
        this.worker = worker;
        this.game = game;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("WorkerControl.fxml"));

        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }
        catch(IOException ex){
            throw new RuntimeException(ex);
        }

        configure();
    }

    private void configure(){
        setText(worker.getName() + " (Poziom " + worker.getLevel() + ")");

        workerImage.setImage(worker.getImage());
        
        workerPrice.setText(doubleValueFormat.format(worker.getPrice()) + "$");
        workerPrice.setFont(WORKER_PRICE_FONT);
        workerPrice.setLayoutY(workerImage.getFitHeight());
        
        workerLevel.setText("Lv. " + worker.getLevel());
        workerLevel.setFont(WORKER_LEVEL_FONT);
        workerLevel.setLayoutX(workerImage.getFitWidth() + MODIFIED_PIXELS_ON_INTERACT);
        workerLevel.setLayoutY(workerImage.getFitHeight() / 2);
    }

    @FXML private void onMouseClicked(MouseEvent event){
        event.consume();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kupno pracownika");

        if (game.upgrade(worker)) {
            alert.setHeaderText("Zakup " + worker.getName() + " zakończony sukcesem.");

            AbstractBoost boost = game.tryApplyRandomBoost(worker);
            if (boost != null)
                alert.setContentText("Dodatkowo otrzymano bonus:\n" + boost.toString());

            workerLevel.setText("Lv. " + worker.getLevel());
            setText(worker.getName() + " (Poziom " + worker.getLevel() + ")");
            workerPrice.setText(worker.getPrice() + "$");
            worker.playSound();
        }
        else {
            alert.setHeaderText("Nie możesz sobie pozwolić na zakup " + worker.getName() + ".");
        }
        alert.show();
    }

    @FXML public void onMouseEntered(){
        workerImage.setFitWidth(workerImage.getFitWidth() + MODIFIED_PIXELS_ON_INTERACT);
        workerImage.setFitHeight(workerImage.getFitHeight() + MODIFIED_PIXELS_ON_INTERACT);
    }

    @FXML public void onMouseExited(){
        workerImage.setFitWidth(workerImage.getFitWidth() - MODIFIED_PIXELS_ON_INTERACT);
        workerImage.setFitHeight(workerImage.getFitHeight() - MODIFIED_PIXELS_ON_INTERACT);
    }
}
