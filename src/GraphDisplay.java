import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.LinkedList;

public class GraphDisplay {

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
        int scale = 30;
        Group g;
        Circle c;
        for (GraphNode n : graph.getNodes().values()){

            g = new Group();

            c = new Circle(10);
            c.setFill(Color.BLUE);
            c.setCenterX(n.getCoordinate().getX()*scale);
            c.setCenterY(n.getCoordinate().getY()*scale);

            n.setDisplay(c);

            g.getChildren().add(c);

            nodeGroupMap.put(n,g);

            centerGroup.getChildren().add(g);

        }

        //display each edge
        //to keep track of the edges
        HashMap<GraphNode, LinkedList<GraphNode>> edgeCheckList =
                new HashMap<>();

        for (GraphNode n : graph.getNodes().values()){

            Line l;

            edgeCheckList.put(n, new LinkedList<>());

            for (GraphNode e : n.getAdjacentNodes()){

                if (!edgeCheckList.get(n).contains(e)) {

                    edgeCheckList.get(n).add(e);

                    l = new Line();
                    l.setStartX(n.getCoordinate().getX());
                    l.setStartY(n.getCoordinate().getY());

                    l.setEndX(e.getCoordinate().getX());
                    l.setEndY(e.getCoordinate().getY());

                }
            }

        }

    }


    public BorderPane getRoot() {
        return root;
    }
}

