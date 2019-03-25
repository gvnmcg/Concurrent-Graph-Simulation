import javafx.scene.Group;
import javafx.scene.layout.BorderPane;

import java.util.HashMap;
import java.util.LinkedList;

public class GraphDisplay {

    public static int scale = 30;
    private BorderPane root = new BorderPane();
    private HashMap<GraphNode, Group> nodeGroupMap = new HashMap<>();

    private Group centerGroup = new Group();

    GraphDisplay(Graph g){

        root.setCenter(centerGroup);
        initDisplay(g);

    }

    /**
     * make graphical components
     * give each node display reference
     *
     *
     * @param graph
     */
    private void initDisplay(Graph graph) {


        //display each node
        for (GraphNode n : graph.getNodes().values()){

            centerGroup.getChildren().add(n.getDisplay());
        }
        //TODO cannot display any edge
        //display each edge
        for (GraphEdge e : graph.getEdges()){

            centerGroup.getChildren().add(e.getLine());
        }

    }


    public BorderPane getRoot() {
        return root;
    }
}

