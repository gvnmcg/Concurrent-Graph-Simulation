import javafx.scene.paint.Color;
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
        Line l = new Line();

        System.out.println("make line to " + nodePair.getKey() + " to " + nodePair.getValue());

        l.setStartX(nodePair.getKey().getCoordinate().getX() * GraphDisplay.scale);
        l.setStartY(nodePair.getKey().getCoordinate().getY() * GraphDisplay.scale);
        l.setEndX(nodePair.getValue().getCoordinate().getX() * GraphDisplay.scale);
        l.setEndY(nodePair.getValue().getCoordinate().getY() * GraphDisplay.scale);
        l.setFill(Color.BLACK);
        l.setStroke(Color.BLACK);
        return l;
    }

    @Override
    public String toString() {
        return nodePair.getKey().toString() + " " + nodePair.getValue();
    }
}
