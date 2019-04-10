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
    private boolean base = false;

    public static long baseDelay = 1500;
    public static long randVariance = 0;

    // Mailbox for message sending
    private LinkedBlockingQueue<Packet> mailbox = new LinkedBlockingQueue<>();


    /**
     * Creates a GraphNode and initializes the starting values
     *
     * @param coordinate Location of the GraphNode
     */
    GraphNode(Coordinate coordinate) {

        // Assign the initial values
        display = new Circle(10);
        display.setStrokeWidth(3);
        cords = coordinate;

        // Set Status initially, then tell GUI the status
        this.status = NodeStatus.GREEN;
        setStatus(NodeStatus.GREEN);
    }

    /**
     * Sets the base and various GUI elements
     */
    public void setBase() {
        base = true;
        display.setStrokeWidth(5);
        display.setStroke(Color.GRAY);
    }

    /**
     * Add Edge from one node to another.
     *  - Does not need to be called more than once
     *
     * @param node GraphNode to be added to!
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
     * Returns the toString implementation of the Coordinates
     *
     * @return String of unique Node location
     */
    @Override
    public String toString() {
        return cords.toString();
    }

    /**
     * Gets the status of the node
     *
     * @return NodeStatus of Node
     */
    public NodeStatus getStatus() {
        // Synchronize on the status in case setStatus is used.
        synchronized (status) {
            return status;
        }
    }

    /**
     * Sets the staus and therefore changing the GUI components
     *
     * @param status Status of the Node
     */
    public void setStatus(NodeStatus status) {
        // Synchronize and change the status
        synchronized (this.status) {
            this.status = status;
        }

        // Platform Run Later allows GUI to synchronously execute the display changes
        Platform.runLater(() -> {

            // Change the color based on the status
            switch (status) {
                case GREEN:
                    display.setFill(Color.BLUE);
                    if (!base) display.setStroke(Color.DARKBLUE);
                    break;
                case YELLOW:
                    display.setFill(Color.YELLOW);
                    if (!base) display.setStroke(Color.ORANGE);
                    break;
                case RED:
                    display.setFill(Color.RED);
                    if (!base) display.setStroke(Color.DARKRED);
                    break;
            }
        });

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
        // Set the center and return the Circle component
        display.setCenterX(getCoordinate().getX() * GraphDisplay.scale);
        display.setCenterY(getCoordinate().getY() * GraphDisplay.scale);
        return display;
    }

    /**
     * Processes a message given within the packet
     *
     * @param p Packet to be sent
     */
    public void sendMessage(Packet p) {
        // Remove the item of focus
        mailbox.remove(p);

        // Checks if the node is the base station
        if (base) {
            // Processes the message of the packet and sets to finished
            System.out.println("LOG: " + p.getMessage());
            p.setFinished();
            synchronized (p){
                Platform.runLater(()->{
                    GraphDisplay.addToLog(p.getMessage());
                });
            }

            return;

            // TODO GraphDisplay stuff
//            GraphDisplay.addToLog(p.message);
        }

        // Checks to see if it needs to be sent to the base station
        if (p.isMessage()) {

            // Count the amount that is navigable
            int notNavigable = 0;

            // Loop through the nodes for checking!
            for (GraphNode node : getAdjacentNodes()) {
                if (!p.contains(node) && node.getStatus() != NodeStatus.RED &&
                    !p.getStatus() && !p.containsTried(node)) {

                    // Adds to the queue and breaks
                    p.addToBQ(this);
                    node.addPacket(p);
                    break;
                } else notNavigable++;
            }

            // Runs if all possible options have failed, backtracks up a node
            // for further testing/examination
            if (notNavigable == adjacentNodes.size() && this != p.getSender()) {
                p.setFail();
                getReceipt(p);
            }
        }
    }

    /**
     * A receipt is sent to notify the previous person of failure among nodes
     * that it has send to.
     *
     * @param p Packet to be processed
     */
    private void getReceipt(Packet p) {
        GraphNode next = p.getLast();

        // Typically, this involves backtracking to notify the previous of
        // potential failure among the nodes
        if (adjacentNodes.contains(next)) {
            next.addPacket(p);
        } else if (this != next) {
            // If it is not contained, then add node back onto the BQ
            p.addToBQ(next);
        }
    }

    /**
     * For each message in the mailbox, attempt to process!
     */
    private void processMessages() {
        // Give receipts the highest authority
        for (Packet p : mailbox) if (p.getStatus()) sendMessage(p);

        // Process the rest of the messages (Receipts may be included)
        for (Packet p : mailbox) sendMessage(p);
    }


    /**
     * Adds the packets to the mailbox and notifies for processing
     *
     * @param p Packet
     */
    public void addPacket(Packet p) {
        // Add to this's mailbox
        mailbox.add(p);

        // Synchronize on itself and notify
        synchronized (this) {
            this.notify();
        }
    }

    /**
     * Runs the thread and assumes its respective role as a GraphNode
     *
     * Messaging and processes are further documented within the readme and
     * associated documents
     */
    @Override
    public void run() {
        // Process messages
        processMessages();

        // While the node is green, wait until notified that neighbor is red
        while (getStatus() == NodeStatus.GREEN) {
            try {
                // Process messages within the mailbox
                processMessages();

                // Wait if mailbox is not of proper size
                if (mailbox.size() == 0) {
                    synchronized (this) {
                        wait();
                    }
                }

                // Process messages within the mailbox
                processMessages();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Yields thread to others
            Thread.yield();
        }

        // Checks if the status is green and changes accordingly
        if (getStatus() == NodeStatus.GREEN) {
            setStatus(NodeStatus.YELLOW);
        }

        // Notify potential mobile agent of the status change
        if (mobileAgent != null) {
            synchronized (mobileAgent) {
                // Notify on the mobileAgent
                mobileAgent.notify();
            }
        }

        // Process any mail traffic
        processMessages();


        // If the status is in danger, then wait the specified time and turn red
        if (getStatus() == NodeStatus.YELLOW) {

            // Wait to catch on fire
            try {
                processMessages();

                Thread.yield();

                int random = (int)(Math.random() * randVariance)+1;

                Thread.sleep(baseDelay+random);

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

        // Waits for the mobile agents "is dead" message.
        //
        // Can remove this without consequence to the program, its just
        // taking advantage of the "Speak one last time" rule
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Process the last messages from the stuff
        processMessages();

        // Thread dies here and all communication is lost after this
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

    /**
     * Update the graphics with the given condition
     *
     * @param gc Updates with the GraphicsContext
     */
    public void updateGraphics(GraphicsContext gc) {

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

        // Fill in the shape
        gc.fillOval(cords.getX(), cords.getY(), 10, 10);

        // Condition based on mobile agent
        if (mobileAgent != null){
            gc.setFill(Color.GREEN);
            gc.strokeOval(cords.getX(), cords.getY(), 15, 15);
        }

    }


}
