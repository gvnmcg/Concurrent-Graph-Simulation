import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Pair;

/**
 * Connects the two GraphNodes specified.
 */
public class GraphEdge {

    // Pair Data structure
    private Pair<GraphNode, GraphNode> nodePair;

    /**
     * Create a pair of edges between the GraphNodes
     *
     * @param n1 GraphNode1
     * @param n2 GraphNode2
     */
    GraphEdge(GraphNode n1, GraphNode n2){
        nodePair = new Pair<>(n1, n2);
        getOther(n1);
    }

    /**
     * Returns the other node, given a node
     *
     * @param n graphNode
     * @return GraphNode of the other one!
     */
    private GraphNode getOther(GraphNode n) {

        // Gets the other given one node
        if (n.equals(nodePair.getKey())){
            return nodePair.getValue();
        } else if (n.equals(nodePair.getValue())){
            return nodePair.getKey();
        } else {
            return null;
        }

    }

    /**
     * Gets the Line that is created between the two GraphNodes
     *
     * @return GUI Line component
     */
    public Line getLine() {
        Line l = new Line();

        // Set the GUI component
        l.setStartX(nodePair.getKey().getCoordinate().getX() * GraphDisplay.scale);
        l.setStartY(nodePair.getKey().getCoordinate().getY() * GraphDisplay.scale);
        l.setEndX(nodePair.getValue().getCoordinate().getX() * GraphDisplay.scale);
        l.setEndY(nodePair.getValue().getCoordinate().getY() * GraphDisplay.scale);
        l.setFill(Color.BLACK);
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(4);
        return l;
    }

    /**
     * To String implementation of the pair
     *
     * @return String with information on the pairing
     */
    @Override
    public String toString() {
        return nodePair.getKey().toString() + " " + nodePair.getValue();
    }

}
