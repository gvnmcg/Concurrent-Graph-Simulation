import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;

public class GraphDisplay {

    private BorderPane root = new BorderPane();
    private HashMap<GraphNode, Group> nodeGroupMap = new HashMap<>();

    private Group centerGroup = new Group();

    GraphDisplay(Graph g){

        root.setCenter(centerGroup);
        initDisplay(g);

    }

    private void initDisplay(Graph graph) {

        int scale = 30;
        Group g;
        Circle c;
        for (GraphNode n : graph.getNodes().values()){

            g = new Group();

            c = new Circle(10);
            c.setFill(Color.BLUE);
            c.setCenterX(n.getCoordinate().getX()*scale);
            c.setCenterY(n.getCoordinate().getY()*scale);

            g.getChildren().add(c);

            nodeGroupMap.put(n,g);

            centerGroup.getChildren().add(g);

        }

    }


    public BorderPane getRoot() {
        return root;
    }
}

