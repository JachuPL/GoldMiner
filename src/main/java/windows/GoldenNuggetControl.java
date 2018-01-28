package windows;

import abstractions.boosts.AbstractBoost;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import services.GameManager;

import java.io.IOException;
import java.text.DecimalFormat;

public class GoldenNuggetControl extends AnchorPane {
    private static final DecimalFormat doubleValueFormat = new DecimalFormat("0.00");
    private static final double MODIFIED_PIXELS_ON_INTERACT = 6;
    private static final double MODIFIED_PIXELS_ON_CLICK = 3;
    @FXML public ImageView goldenNuggetImage;
    @FXML public Label gatheredOunces;
    @FXML public ImageView goldOuncesImage;

    private GameManager game;

    public GoldenNuggetControl(){

        FXMLLoader loader = new FXMLLoader(getClass().getResource("GoldenNuggetControl.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try{
            loader.load();
        }
        catch(IOException ex){
            throw new RuntimeException(ex);
        }
    }

    public void configure(GameManager game){
        this.game = game;
        goldenNuggetImage.setImage(new Image(game.GOLDEN_NUGGET_IMAGE_PATH));
        goldenNuggetImage.setFitHeight(150);
        goldenNuggetImage.setFitWidth(150);

        goldOuncesImage.setImage(new Image(game.GOLDEN_NUGGET_IMAGE2_PATH));
    }

    @FXML private void onMouseClicked(MouseEvent event){
        event.consume();
        if (event.getButton() == MouseButton.PRIMARY) {
            game.harvestOnClick();
            updateScore();
            AbstractBoost boost = game.tryApplyRandomBoost(null);
            if (boost != null){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Bonus!");
                alert.setHeaderText("Otrzymano bonus:\n" + boost.toString());
                alert.show();
            }
        }
    }

    @FXML public void onMouseEntered(){
        goldenNuggetImage.setLayoutX(goldenNuggetImage.getLayoutX() - MODIFIED_PIXELS_ON_CLICK);
        goldenNuggetImage.setLayoutY(goldenNuggetImage.getLayoutY() - MODIFIED_PIXELS_ON_CLICK);
        goldenNuggetImage.setFitWidth(goldenNuggetImage.getFitWidth() + MODIFIED_PIXELS_ON_INTERACT);
        goldenNuggetImage.setFitHeight(goldenNuggetImage.getFitHeight() + MODIFIED_PIXELS_ON_INTERACT);
    }

    @FXML public void onMouseExited(){
        goldenNuggetImage.setLayoutX(goldenNuggetImage.getLayoutX() + MODIFIED_PIXELS_ON_CLICK);
        goldenNuggetImage.setLayoutY(goldenNuggetImage.getLayoutY() + MODIFIED_PIXELS_ON_CLICK);
        goldenNuggetImage.setFitWidth(goldenNuggetImage.getFitWidth() - MODIFIED_PIXELS_ON_INTERACT);
        goldenNuggetImage.setFitHeight(goldenNuggetImage.getFitHeight() - MODIFIED_PIXELS_ON_INTERACT);
    }

    public void updateScore(){
        gatheredOunces.setText(doubleValueFormat.format(game.getHarvestedCoins()) + " uncji");
    }
}
