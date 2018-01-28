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
    private static final Font PRICE_FONT = Font.font("Arial", 16);
    private static final Font LEVEL_FONT = Font.font("Arial", 24);
    private static final double MODIFIED_PIXELS_ON_INTERACT = 5;
    private static final DecimalFormat doubleValueFormat = new DecimalFormat("0.00");

    @FXML private Label level;
    @FXML private Label price;
    @FXML private ImageView image;
    @FXML private AnchorPane anchorPane;
    @FXML private Label nonClickingBase;
    @FXML private Label nonClickingMultiplier;
    @FXML private Label clickingBase;
    @FXML private Label clickingMultiplier;

    private AbstractWorker worker;

    WorkerControl(AbstractWorker worker){
        this.worker = worker;

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
        image.setImage(worker.getImage());

        price.setFont(PRICE_FONT);
        price.setLayoutY(image.getFitHeight());

        level.setFont(LEVEL_FONT);
        level.setLayoutX(image.getFitWidth() + MODIFIED_PIXELS_ON_INTERACT);
        level.setLayoutY(image.getFitHeight() / 2);

        clickingBase.setLayoutX(level.getLayoutX() + level.getWidth() + 85);

        clickingMultiplier.setLayoutX(clickingBase.getLayoutX());
        clickingMultiplier.setLayoutY(clickingBase.getLayoutY() + 20);

        nonClickingBase.setLayoutX(clickingMultiplier.getLayoutX());
        nonClickingBase.setLayoutY(clickingMultiplier.getLayoutY() + 20);

        nonClickingMultiplier.setLayoutX(nonClickingBase.getLayoutX());
        nonClickingMultiplier.setLayoutY(nonClickingBase.getLayoutY() + 20);

        update();
    }

    public void update(){
        setText(worker.getName() + " (Poziom " + worker.getLevel() + ")");
        price.setText(doubleValueFormat.format(worker.getPrice()) + "$");
        level.setText("Lv. " + worker.getLevel());

        if (worker.getLevel() > 0)
        {
            clickingBase.setText("Uncji za kliknięcie: " + doubleValueFormat.format(worker.getBoostedUnitsPerSecForClicking()));
            clickingMultiplier.setText("Mnożnik uncji za kl.: " + doubleValueFormat.format(worker.getBoostedUnitsPerSecMultiplierForClicking()));
            nonClickingBase.setText("Uncji na sekundę: " + doubleValueFormat.format(worker.getBoostedUnitsPerSecForNonClicking()));
            nonClickingMultiplier.setText("Mnożnik uncji na sek.: " + doubleValueFormat.format(worker.getBoostedUnitsPerSecMultiplierForNonClicking()));
        }
        else
        {
            clickingBase.setText("Ten pracownik obecnie nie generuje przychodu.");
            clickingMultiplier.setText("Ten pracownik obecnie nie generuje przychodu.");
            nonClickingBase.setText("Ten pracownik obecnie nie generuje przychodu.");
            nonClickingMultiplier.setText("Ten pracownik obecnie nie generuje przychodu.");
        }
    }

    @FXML private void onMouseClicked(MouseEvent event){
        event.consume();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Kupno pracownika");

        if (GameManager.instance().upgrade(worker)) {
            alert.setHeaderText("Zakup " + worker.getName() + " zakończony sukcesem.");

            AbstractBoost boost = GameManager.instance().tryApplyRandomBoost(worker);
            if (boost != null)
                alert.setContentText("Dodatkowo otrzymano bonus:\n" + boost.toString());

            update();
            worker.playSound();
        }
        else {
            alert.setHeaderText("Nie możesz sobie pozwolić na zakup " + worker.getName() + ".");
        }
        alert.show();
    }

    @FXML public void onMouseEntered(){
        image.setFitWidth(image.getFitWidth() + MODIFIED_PIXELS_ON_INTERACT);
        image.setFitHeight(image.getFitHeight() + MODIFIED_PIXELS_ON_INTERACT);
    }

    @FXML public void onMouseExited(){
        image.setFitWidth(image.getFitWidth() - MODIFIED_PIXELS_ON_INTERACT);
        image.setFitHeight(image.getFitHeight() - MODIFIED_PIXELS_ON_INTERACT);
    }
}
