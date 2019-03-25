import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.LinkedList;


public class GraphNode implements Runnable {

    private LinkedList<GraphNode> adjacentNodes = new LinkedList<>();
    private Coordinate cords;
    private NodeStatus status;

    private Circle display;


    GraphNode(int x, int y) {
        cords = new Coordinate(x,y);
        status = NodeStatus.GREEN;
    }

    GraphNode(Coordinate coordinate) {
        cords = coordinate;
        status = NodeStatus.GREEN;
    }

    public void addEdge(GraphNode node) {
        adjacentNodes.add(node);
        node.getAdjacentNodes().add(this);
    }

    public LinkedList<GraphNode> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void printNeighbors() {
        for (GraphNode node : adjacentNodes) {
            System.out.print(node.toString() + " ");
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return cords.toString();
    }

    public synchronized NodeStatus getStatus() {
            return status;
    }

    public synchronized void setStatus(NodeStatus status) {

        switch (status){
            case GREEN:
                display.setFill(Color.BLUE);
                break;
            case YELLOW:
                display.setFill(Color.YELLOW);
                break;
            case RED:
                display.setFill(Color.RED);
                break;
        }
            this.status = status;
    }

    public Coordinate getCoordinate() {
        return cords;
    }

    public void setDisplay(Circle display) {

        this.display = display;

    }

    public Circle getDisplay() {
//        return display;

        Circle c ;
        c = new Circle(10);
        c.setFill(Color.BLUE);
        c.setCenterX(getCoordinate().getX()*GraphDisplay.scale);
        c.setCenterY(getCoordinate().getY()*GraphDisplay.scale);

        return c;


    }

    @Override
    public void run() {

        System.out.println("Node: " + toString() + "; Status: " + status + " 1");
        while (status == NodeStatus.GREEN) {
            // Check status of other nodes
            for (GraphNode node : adjacentNodes) {

                if (node.getStatus() == NodeStatus.RED) {
                    System.out.println(toString() + " found out that it's neighbor " + node.toString() + " is on fire!");
                    status = NodeStatus.YELLOW;
                    break;
                }
            }
            Thread.yield();
        }

        if (status == NodeStatus.YELLOW) {
            System.out.println("Node: " + toString() + "; Status: " + status + " 2");
            try {
                Thread.yield();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Tried sleeping!");
                e.printStackTrace();
            }
        }


        status = NodeStatus.RED;
        System.out.println("Node: " + toString() + "; Status: " + status + " 3");
        // Is red, need to notify others o
    }
}
