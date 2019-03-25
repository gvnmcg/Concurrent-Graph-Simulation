import javafx.scene.shape.Line;
import javafx.util.Pair;

public class GraphEdge {

    private Pair<GraphNode, GraphNode> nodePair;

    GraphEdge(GraphNode n1, GraphNode n2){

        nodePair = new Pair<>(n1, n2);

        getOther(n1);
    }

    private GraphNode getOther(GraphNode n) {

        if (n.equals(nodePair.getKey())){
            return nodePair.getValue();
        } else if (n.equals(nodePair.getValue())){
            return nodePair.getKey();
        } else {
            return null;
        }

    }

    public Line getLine() {
        return new Line(nodePair.getKey().getCoordinate().getX(),
                nodePair.getKey().getCoordinate().getY(),
                nodePair.getValue().getCoordinate().getX(),
                nodePair.getValue().getCoordinate().getY());
    }
}
