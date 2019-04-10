import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Pair;

/**
 * connects two GraphNodes
 */
public class GraphEdge {

    private Pair<GraphNode, GraphNode> pair;

    GraphEdge(GraphNode n1, GraphNode n2){

        pair = new Pair<>(n1, n2);

        getOther(n1);
    }

    /**
     * returns the other node, given a node
     * @param n
     * @return
     */
    private GraphNode getOther(GraphNode n) {

        if (n.equals(pair.getKey())){
            return pair.getValue();
        } else if (n.equals(pair.getValue())){
            return pair.getKey();
        } else {
            return null;
        }

    }

    public Line getLine() {
        Line l = new Line();

        l.setStartX(pair.getKey().getCoordinate().getX()*GraphDisplay.scale);
        l.setStartY(pair.getKey().getCoordinate().getY()*GraphDisplay.scale);
        l.setEndX(pair.getValue().getCoordinate().getX()*GraphDisplay.scale);
        l.setEndY(pair.getValue().getCoordinate().getY()*GraphDisplay.scale);
        l.setFill(Color.BLACK);
        l.setStroke(Color.BLACK);
        l.setStrokeWidth(4);
        return l;
    }

    @Override
    public String toString() {
        return pair.getKey().toString() + " " + pair.getValue();
    }

    public void updateGraphics(GraphicsContext gc) {
        gc.setFill(Color.BLACK);

        gc.strokeLine(
                pair.getKey().getCoordinate().getX()*GraphDisplay.scale,
                pair.getKey().getCoordinate().getY()*GraphDisplay.scale,
                pair.getValue().getCoordinate().getX()*GraphDisplay.scale,
                pair.getValue().getCoordinate().getY()*GraphDisplay.scale);
    }
}
