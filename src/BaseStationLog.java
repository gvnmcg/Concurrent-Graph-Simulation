import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Observable;

public class BaseStationLog extends VBox {

    ObservableList<Node> list = getChildren();

    BaseStationLog(){
        setSpacing(10);
    }

    public void addMessage(String message){


        list.add(0, new Label(message));
    }
}
