import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BaseStationLog extends VBox {

    BaseStationLog(){

    }

    public void addMessge(String message){

        getChildren().add(new Label(message));
    }
}
