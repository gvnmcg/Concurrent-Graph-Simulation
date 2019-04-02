import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Graph Node for Graph data structure
 */
public class GraphNode implements Runnable {

    // Private variables
    private ArrayList<GraphNode> adjacentNodes = new ArrayList<>();
    private Coordinate cords;
    private NodeStatus status;
    private Circle display;
    private MobileAgent mobileAgent;
    boolean base = false;

    // Mailbox for message sending
    LinkedBlockingQueue<Packet> mailbox = new LinkedBlockingQueue<>();


    /**
     * Creates a GraphNode and initializes the starting values
     *
     * @param coordinate Location of the GraphNode
     */
    GraphNode(Coordinate coordinate) {
        display = new Circle(10);
        cords = coordinate;
        this.status = NodeStatus.GREEN;
        setStatus(NodeStatus.GREEN);
    }

    /**
     * Adds an edge between this and the node specified in the parameters.
     *
     * @param node Node to create edges between
     */
    public void addEdge(GraphNode node) {
        // Adds the node to the ArrayList
        adjacentNodes.add(node);


        // TODO Check usage of getAdjacentNodes

        // Adds this to the nodes arraylist
        node.getAdjacentNodes().add(this);
    }

    /**
     * Gets and returns the AdjacentNodes of the graphNode
     *
     * @return AdjacentNodes found in ArrayList<GraphNode>
     */
    public ArrayList<GraphNode> getAdjacentNodes() {
        return adjacentNodes;
    }

    /**
     * Prints the neighbors of the nodes that are adjacent
     */
    public void printNeighbors() {

        // Synchronizes on adjacent nodes
        synchronized (adjacentNodes) {

            // Prints nodes string representation
            for (GraphNode node : adjacentNodes) {
                System.out.print(node.toString() + " ");
            }
            System.out.println();
        }
    }

    /**
     * Returns the toString implementation of the Coordinates
     *
     * @return String of unique Node location
     */
    @Override
    public String toString() {
        return cords.toString();
    }

    // TODO Verify the needed effects of synchronized in the function block
    /**
     * Gets the status of the node
     *
     * @return NodeStatus of Node
     */
    public synchronized NodeStatus getStatus() {
        // Synchronize on the status in case setStatus is used.
        synchronized (status) {
            return status;
        }
    }

    /**
     * Sets the staus and therefore changing the GUI components
     *
     * @param status
     */
    public synchronized void setStatus(NodeStatus status) {
        // Platform Run Later allows GUI to synchronously execute the display changes
        Platform.runLater(() -> {

            // TODO Verify the synch is needed
            // Synchronize on the display
            synchronized (display) {

                // Change the color based on the status
                switch (status) {
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
            }
        });

        // Synchronize and change the status
        synchronized (this.status) {
            // Change the status
            this.status = status;
        }
    }

    /**
     * Gets the coordinate of the GraphNode
     *
     * @return Coordinate location of node
     */
    public Coordinate getCoordinate() {
        return cords;
    }


    /**
     * Gets the Circle representation of the GraphNode
     *
     * @return Circle representing Display
     */
    public Circle getDisplay() {
        // TODO Why synch
        synchronized(status) {
            // Set the center and return the Circle component
            display.setCenterX(getCoordinate().getX() * GraphDisplay.scale);
            display.setCenterY(getCoordinate().getY() * GraphDisplay.scale);
            return display;
        }
    }

    public void sendMessage(Packet p) {
        if (base == true) {
            System.out.println(p.getMessage());
            p.setFinished();
        }
        if (p.inProgress) {
            p.addToBQ(this);
            for (GraphNode node : adjacentNodes) {
                if (node != this && node.getStatus() != NodeStatus.RED) {
                    node.addPacket(p);
                    node.notify();
                    if (getReceipt(p.getID())) {
                        break;
                    }
                }
            }
        }
        else {
            GraphNode test = p.getLast();
            if (adjacentNodes.contains(test)) {
                test.addPacket(p);
                test.notify();
            } else {
                p.addToBQ(test);
            }
        }
    }

    private void processMessages() {
        for (Packet p : mailbox) {
            sendMessage(p);
            mailbox.remove(p);
        }
    }

    private boolean getReceipt(int num) {
        Packet receipt;
        while ((receipt = checkForReceipt(num)) == null) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return receipt.getStatus();

    }

    private Packet checkForReceipt(int num) {
        for (Packet p : mailbox)
            if (p.getSender() == this && p.getID() == num) return p;
        return null;
    }

    public void addPacket(Packet p) {
        mailbox.add(p);
    }

    @Override
    public void run() {
        // TODO Change to a notify wait program.

        System.out.println("Node: " + toString() + "; Status: " + status + " 1");
        while (getStatus() == NodeStatus.GREEN) {
            try {
                synchronized (this) {
                    wait();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.yield();
        }

        if (getStatus() == NodeStatus.GREEN) {
            setStatus(NodeStatus.YELLOW);
        }

        if (mobileAgent != null) {
            synchronized (mobileAgent) {
                mobileAgent.notify();
            }
        }

        // If node is notified of a fire
        if (getStatus() == NodeStatus.YELLOW) {
            System.out.println("Node: " + toString() + "; Status: " + status + " 2");

            // Wait to catch on fire
            try {
                Thread.yield();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Tried sleeping!");
                e.printStackTrace();
            }
        }


        // On fire
        setStatus(NodeStatus.RED);

        for (GraphNode node : adjacentNodes) {
            synchronized (node) {
                if (node.getStatus() == NodeStatus.GREEN) {
                    node.setStatus(NodeStatus.YELLOW);
                    node.notify();
                }
            }
        }

        if (mobileAgent != null) {
            synchronized (mobileAgent) {
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

    public MobileAgent getMobileAgent() {
        return mobileAgent;
    }

    public void updateGrahics(GraphicsContext gc) {

        // Change the color based on the status
        switch (status) {
            case GREEN:
                gc.setFill(Color.BLUE);
                break;
            case YELLOW:
                gc.setFill(Color.YELLOW);
                break;
            case RED:
                gc.setFill(Color.RED);
                break;
                default:
                    gc.setFill(Color.BLUE);
        }

        gc.fillOval(cords.getX(), cords.getY(), 10, 10);

        if (mobileAgent != null){
            gc.setFill(Color.GREEN);
            gc.strokeOval(cords.getX(), cords.getY(), 15, 15);
        }

    }
}