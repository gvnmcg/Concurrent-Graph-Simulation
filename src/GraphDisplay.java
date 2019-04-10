import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.HashMap;

/**
 * Displays Graph data structure and it components
 */
public class GraphDisplay {

    public static int scale = 50;
    private BorderPane root = new BorderPane();
    private HashMap<GraphNode, Group> nodeGroupMap = new HashMap<>();

    private Group centerGroup = new Group();

    private static BaseStationLog baseLog = new BaseStationLog();

    /**
     * Initializes javafx shapes etc GUI components
     */
    GraphDisplay(Graph g){


        ScrollPane sp = new ScrollPane();
        sp.setContent(baseLog);
        sp.setMaxHeight(350);
        sp.setMinHeight(350);
        sp.setPadding(new Insets(5,0,5,50));

        root.setCenter(centerGroup);
        root.setRight(getLegend());
        root.setBottom(sp);

        initGraphics(g);
    }

    private Node baseStationLog() {

        baseLog = new BaseStationLog();

        return baseLog;
    }

    public static void addToLog(String message){

        baseLog.addMessge(message);
    }

    private VBox getLegend() {

        VBox vBox = new VBox();

        vBox.getChildren().add(new Text("Blue = OK"));
        vBox.getChildren().add(new Text("Yellow = In Danger"));
        vBox.getChildren().add(new Text("Red = On Fire/Destroyed"));

        return vBox;
    }

    /**
     *  Make graphical components
     *  Give each node display reference
     * @param graph
     */
    private void initGraphics(Graph graph) {

        //TODO cannot display any edge

        // Display each edge
        for (GraphEdge e : graph.getEdges()){

            centerGroup.getChildren().add(e.getLine());
        }

        // Display each node
        for (GraphNode n : graph.getNodes().values()){

            centerGroup.getChildren().add(n.getDisplay());

        }

    }



    public void addToCenter(Node thing){
        Platform.runLater(() -> {

            synchronized (thing) {
                centerGroup.getChildren().add(thing);
            }
        });
    }


    public Pane getRoot() {
        return root;
    }

}

