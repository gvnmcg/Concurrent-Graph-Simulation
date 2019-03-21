import java.util.LinkedList;


public class GraphNode implements Runnable {

    private LinkedList<GraphNode> adjacentNodes = new LinkedList<>();
    private Coordinate cords;
    private NodeStatus status;


    GraphNode(int x, int y) {
        cords = new Coordinate(x,y);
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
        synchronized (status) {
            return status;
        }
    }

    public synchronized void setStatus(NodeStatus status) {
        synchronized (status) {
            this.status = status;
        }

    }

    public Coordinate getCoordinate() {
        return cords;
    }

    @Override
    public void run() {

        System.out.println("Node: " + toString() + "; Status: " + status + " 1");
        while (status == NodeStatus.GREEN) {
//            System.out.print(toString() + " ");
            // Check status of other nodes
            for (GraphNode node : adjacentNodes) {
                if (node.getStatus() == NodeStatus.RED) {
                    System.out.println(toString() + " found out that it's neighbor " + node.toString() + " is on fire!");
                    status = NodeStatus.YELLOW;
                    break;
                }
            }

        }

        if (status == NodeStatus.YELLOW) {
            System.out.println("Node: " + toString() + "; Status: " + status + " 2");
            try {
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
