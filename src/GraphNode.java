import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.ArrayList;

public class GraphNode implements Runnable {

    private ArrayList<GraphNode> adjacentNodes = new ArrayList<>();
    private Coordinate cords;
    private NodeStatus status;

    private Circle display;
    private MobileAgent mobileAgent;


    GraphNode(int x, int y) {
        display = new Circle(10);
        cords = new Coordinate(x,y);
        setStatus(NodeStatus.GREEN);
    }

    GraphNode(Coordinate coordinate) {
        display = new Circle(10);
        cords = coordinate;
        setStatus(NodeStatus.GREEN);
    }

    public void addEdge(GraphNode node) {
        adjacentNodes.add(node);
        node.getAdjacentNodes().add(this);
    }

    public ArrayList<GraphNode> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void printNeighbors() {
        synchronized (adjacentNodes) {
            for (GraphNode node : adjacentNodes) {
                System.out.print(node.toString() + " ");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return cords.toString();
    }

    public synchronized NodeStatus getStatus() {
        synchronized (status) {
            return status;
        }
    }

    public synchronized void setStatus(NodeStatus status) {
        synchronized (status) {

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
    }

    public Coordinate getCoordinate() {
        return cords;
    }

//    public void setDisplay(Circle display) {
//
//        this.display = display;
//
//    }

    public synchronized Circle getDisplay() {
        synchronized(status) {
            display.setCenterX(getCoordinate().getX() * GraphDisplay.scale);
            display.setCenterY(getCoordinate().getY() * GraphDisplay.scale);
            return display;
        }
//        Circle c ;


//        return c;


    }

    @Override
    public void run() {
        // TODO Change to a notify wait program.

        System.out.println("Node: " + toString() + "; Status: " + status + " 1");
        while (getStatus() == NodeStatus.GREEN) {

            // Check status of other nodes
            synchronized (adjacentNodes) {

                //check adjecent nodes for fires
                for (GraphNode node : adjacentNodes) {

                    if (node.getStatus() == NodeStatus.RED) {
                        System.out.println(toString() + " found out that it's neighbor " + node.toString() + " is on fire!");
                        setStatus(NodeStatus.YELLOW);

                        //if there is a agent, notify the agent
                        if (mobileAgent != null) {
                            synchronized (mobileAgent) {
                                System.out.println("Notifying MA");
                                mobileAgent.notify(getStatus());
                                mobileAgent.notify();
                            }
                        }
                        break;
                    }
                }
            }
            Thread.yield();
        }

        //if node is notified of a fire
        if (getStatus() == NodeStatus.YELLOW) {
            System.out.println("Node: " + toString() + "; Status: " + status + " 2");

            //wait to catch on fire
            try {
                Thread.yield();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Tried sleeping!");
                e.printStackTrace();
            }
        }


        //on fire
        setStatus(NodeStatus.RED);
        if (mobileAgent != null) {
            synchronized (mobileAgent) {
                mobileAgent.notify(getStatus());
                mobileAgent.notify();
            }
        }
        System.out.println("Node: " + toString() + "; Status: " + status + " 3");
        // Is red, need to notify others o
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public void setMobileAgent(MobileAgent mobileAgent) {
        this.mobileAgent = mobileAgent;

    }
}
