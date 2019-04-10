import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Displays Graph data structure and it components
 */
public class GraphDisplay {

    public static int scale = 50;
    private BorderPane root = new BorderPane();
    private Group centerGroup = new Group();
    private static BaseStationLog baseLog = new BaseStationLog();


    /**
     * Initializes javafx shapes etc GUI components
     */
    GraphDisplay(Graph g){


        ScrollPane sp = new ScrollPane();
        sp.setContent(baseLog);
        sp.setMaxHeight(300);
        sp.setMinHeight(300);
        sp.setPadding(new Insets(5,0,5,50));

        root.setCenter(centerGroup);
        root.setRight(getLegend());
        root.setBottom(sp);

        initGraphics(g);
    }

    /**
     * Add to the log with the specified message
     *
     * @param message Message of the string
     */
    public static void addToLog(String message){

        baseLog.addMessage(message);
    }

    /**
     * Get the legend, which tells the user what is dead/indanger/alive
     *
     * @return VBox of labels
     */
    private VBox getLegend() {
        VBox vBox = new VBox();

        // Set the Text of the fields
        vBox.getChildren().add(new Text("Blue = OK"));
        vBox.getChildren().add(new Text("Yellow = In Danger"));
        vBox.getChildren().add(new Text("Red = On Fire/Destroyed"));

        return vBox;
    }

    /**
     *  Make graphical components
     *  Give each node display reference
     * @param graph Graph
     */
    private void initGraphics(Graph graph) {

        // Display each edge
        for (GraphEdge e : graph.getEdges()){
            centerGroup.getChildren().add(e.getLine());
        }

        // Display each node
        for (GraphNode n : graph.getNodes().values()){
            centerGroup.getChildren().add(n.getDisplay());
        }

    }

    /**
     * Add to the center of the Node
     *
     * @param thing Node
     */
    public void addToCenter(Node thing){
        // Synchronized or else would give, Illegal State exception
        Platform.runLater(() -> {
            synchronized (thing) {
                centerGroup.getChildren().add(thing);
            }
        });
    }

    /**
     * Gets the root of the emulation
     *
     * @return Returns the root
     */
    public Pane getRoot() {
        return root;
    }

}

