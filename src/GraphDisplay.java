import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.HashMap;

/**
 * Displays Graph data structure and it components
 */
public class GraphDisplay {

    public static int scale = 50;
    private BorderPane root = new BorderPane();
    private HashMap<GraphNode, Group> nodeGroupMap = new HashMap<>();

    private Group centerGroup = new Group();

    private Graph graph;

    GraphicsContext gc;

    GraphDisplay(Graph g){
        this.graph = g;

        root.setCenter(centerGroup);
        initDisplay(g);

//        AnimationTimer aTimer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                refreshGraphView();
//            }
//        }

    }

    GraphDisplay(Graph g, boolean b ){
        Canvas canvas = new Canvas();
        root.setCenter(canvas);

        initDisplay(gc);


    }

    private void initDisplay(GraphicsContext gc) {

    }

    /**
     *  Make graphical components
     *  Give each node display reference
     * @param graph
     */
    private void initDisplay(Graph graph) {

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

