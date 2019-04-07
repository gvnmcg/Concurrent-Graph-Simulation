import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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

    GraphDisplay(){

    }

    public void initDisplay(Graph graph){
        this.graph = graph;
        root.setCenter(centerGroup);
        initGraphics(graph);
    }

    /**
     * Initializes javafx shapes etc GUI components
     */
    GraphDisplay(Graph g){
        this.graph = g;

        root.setCenter(centerGroup);
        initGraphics(g);

//        AnimationTimer aTimer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                refreshGraphView();
//            }
//        }

    }

    /**
     * intialized GUI with graphics context
     * @param g
     * @param b
     */
    GraphDisplay(Graph g, boolean b ){
        Canvas canvas = new Canvas(700, 500);
        root.setCenter(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.strokeOval(10, 10, 10, 10);

        initGraphics(g, gc);

        System.out.println("huh?");

    }

    private void initGraphics(Graph g, GraphicsContext gc) {


        for (GraphEdge e : g.getEdges()){
            e.updateGraphics(gc);
        }

        for (GraphNode n : g.getNodes().values()){
            n.updateGrahics(gc);
        }

        Thread displayThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){

                    for (GraphNode n : g.getNodes().values()){
                        n.updateGrahics(gc);
                    }
                }

            }
        });
        displayThread.start();
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

    public static void main(String[] args) {
        // Initialize gragh data structure
        Graph graph = new Graph("resources/TestForLock");

        // Display graph
//        GraphDisplay graphDisplay = new GraphDisplay(graph);
        GraphDisplay graphDisplay = new GraphDisplay(graph, true);
    }
}

