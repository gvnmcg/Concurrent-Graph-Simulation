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

    public void setBase() {
        base = true;
    }

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
            synchronized (display){

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

                if (base) display.setFill(Color.LIGHTGREEN);
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

    /**
     * Processes a message given within the packet
     *
     * @param p
     */
    public void sendMessage(Packet p) {
        // Checks if the node is the base station
        if (base) {
            // Processes the message of the packet
            if (Main.debugMessaging) System.out.println("BASE STATION (" + this + ") REPORT: " + p.getMessage());
            // Sets it to be finished
            p.setFinished();
        }
        System.out.println("Packet about to be processed " + p);
        // Checks to see if it needs to be sent to the base station
        if (p.isMessage()) {
            p.addToBQ(this);

            int numNavigable = 0;
            for (GraphNode node : adjacentNodes) {
                if (!p.contains(node) && node.getStatus() != NodeStatus.RED && !p.getStatus()) {
                    node.addPacket(p);
                    System.out.println("ADDING PACKET");
                    synchronized (node) { node.notify(); }
                    if (getReceipt(p.getID())) {
//                        getReceipt(p);
                        if (p.getSender() == this && p.getStatus()) System.out.println("Maybe it worked");
                        synchronized (node) {node.notify();}
                        if (Main.debugMessaging) System.out.println("Broke @ " + this);
                        break;
                    }
                    if (Main.debugMessaging) System.out.println("IT KEPT GOING FOR PACKET #" + p.getID());
                } else {
                    System.out.println("Innavigable @ " + node + " " + node.getStatus() + " " + node.getMobileAgent());
                    numNavigable++;
                }
            }
            System.out.println("SIZE CHECK OF (" + this + "): " +numNavigable + " " + adjacentNodes.size());
            if (numNavigable == adjacentNodes.size()) {
                if (Main.debugMessaging) {
                    System.out.println("CANT TRAVERSE @ " + this + " for the packet: " + p);
                    p.printBQ();
                }

                p.setFail();
                // Means that there is no node under "this", that is connected to the base station.
            }
        }
        else {
            getReceipt(p);
        }
//        if (Main.debugMessaging) System.out.println("Finished sending!");
    }

    private void getReceipt(Packet p) {
        p.printBQ();
        GraphNode test = p.getLast();
        System.out.println("Packet is traversing on " + this + " heading towards " + test);
        if (this == test) {
            if (Main.debugMessaging) System.out.println("RECEIPT #" + p.getID() + " SUCCESSFULLY RECEIVED!");
        }
        else if (adjacentNodes.contains(test)) {
            if (Main.debugMessaging) System.out.println("RETURNING RECEIPT @ (" + this + ") to (" + test + "): " + p);
            test.addPacket(p);
            if (Main.debugMessaging) System.out.println("Packet was added to " + test);
//                synchronized (test) { test.notify(); }
        } else {

            if (Main.debugMessaging) System.out.println("FUCK @ (" + this + ") to (" + test + "): " + p);
            p.addToBQ(test);
        }
    }

    /**
     * For each message in the mailbox, attempt to process!
     */
    private void processMessages() {
        for (Packet p : mailbox) {
            if (p.getStatus()) {
                if (Main.debugMessaging) System.out.println("Processing Receipt @ " + this + " | " + p);
                // Act on message
                sendMessage(p);

                // Remove message
                mailbox.remove(p);
            }

        }
        for (Packet p : mailbox) {
            if (Main.debugMessaging) System.out.println("Processing Message @ " + this + " | " + p);
            // Act on message
            sendMessage(p);

            // Remove message
            mailbox.remove(p);
        }
    }

    /**
     * Wait until a receipt is received with same ID Number
     *
     * @param num ID of receipt that is going to be retrieved.
     * @return Returns true if base station got the message, false if not
     */
    private boolean getReceipt(int num) {
        Packet receipt;

        // Check if there is a receipt matching the ID Number
        while ((receipt = checkForReceipt(num)) == null) {
            try {
                if (Main.debugMessaging) System.out.println("WAITING @ " + this + " with " + receipt);
                // Wait, and get notified when mailbox gets put in
                synchronized (this) {
                    wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Returns status
        return receipt.getStatus();
    }


    /**
     * Loops through each packet and checks for the receipt
     *
     * @param num
     * @return
     */
    private Packet checkForReceipt(int num) {
        // TODO, This should not need the p.getStatus() as there are times a receipt can be false and have the same ID
        for (Packet p : mailbox) {
            if (p.getID() == num && !p.isMessage()) {
                System.out.println("CHECK FOR RECEIPT (NUM:" + num + "): " + p);
                return p;
            }
        }
        return null;
    }

    /**
     * Adds the packets to the mailbox
     *
     * @param p Packet
     */
    public void addPacket(Packet p) {
        mailbox.add(p);
        // Synchronize and notify
        synchronized (this) {
            this.notify();
            if (Main.debugMessaging) System.out.println("Notified " + this);
        }
    }

    /**
     * Runs the thread and assumes its respective roles
     */
    @Override
    public void run() {
        if (Main.debugGraphNode) System.out.println("Node: " + toString() + "; Status: " + status + " 1");

        // While the node is green, wait until notfied that neighbor is red
        processMessages();
        while (getStatus() == NodeStatus.GREEN) {
            try {
//                processMessages();
                synchronized (this) {
                    wait();
                }
                processMessages();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Yields thread to others
            Thread.yield();
        }
        processMessages();
        if (getStatus() == NodeStatus.GREEN) {
            setStatus(NodeStatus.YELLOW);
        }
        if (mobileAgent != null) {
            synchronized (mobileAgent) {
                // Notify on the mobileAgent
                mobileAgent.notify();
                processMessages();
            }
        }

        // If node is notified of a fire
        if (getStatus() == NodeStatus.YELLOW) {
            if (Main.debugGraphNode) System.out.println("Node: " + toString() + "; Status: " + status + " 2");

            // Wait to catch on fire
            try {
                processMessages();

                Thread.yield();
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        processMessages();

        // Sets status onto fire
        setStatus(NodeStatus.RED);

        // Sets the adjacent nodes to yellow
        for (GraphNode node : adjacentNodes) {
            synchronized (node) {
                if (node.getStatus() == NodeStatus.GREEN) {
                    node.setStatus(NodeStatus.YELLOW);
                    node.notify();
                }
            }
        }

        // Notify the mobile agent
        if (mobileAgent != null) {
            synchronized (mobileAgent) {
                mobileAgent.notify();
            }
        }

        // Print final state
        if (Main.debugGraphNode) System.out.println("Node: " + toString() + "; Status: " + status + " 3");
    }

    /**
     * Overwrites the hashing algorithm to allow for equality to be checked
     * in maps.
     *
     * @return Int of hashcode
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Set Mobile Agent
     *
     * @param mobileAgent MA
     */
    public void setMobileAgent(MobileAgent mobileAgent) {
        this.mobileAgent = mobileAgent;
    }

    /**
     * Get the Mobile Agent
     *
     * @return Mobile Agent
     */
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

        gc.fillOval(cords.getX()* GraphDisplay.scale,
                cords.getY()* GraphDisplay.scale,
                10* GraphDisplay.scale,
                10* GraphDisplay.scale
        );

        if (mobileAgent != null){
            gc.setFill(Color.GREEN);
            gc.strokeOval(cords.getX(), cords.getY(), 15, 15);
        }

    }
}