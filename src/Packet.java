import java.util.concurrent.LinkedBlockingDeque;

public class Packet {

    private String message;
    private boolean success;
    private boolean msg;
    private LinkedBlockingDeque<GraphNode> bq = new LinkedBlockingDeque<>();
    private LinkedBlockingDeque<GraphNode> tried = new LinkedBlockingDeque<>();
    private int ID;

    Packet (String message, boolean update, GraphNode gn, int ID) {
        success = false;
        msg = true;
        this.message = message;
//        this.update = update;
        bq.add(gn);
        this.ID = ID;
    }

    /**
     * Check for node in the tried Queue
     *
     * @param node Node to test
     * @return boolean, true if tested
     */
    public boolean containsTried(GraphNode node) {
        return tried.contains(node);
    }

    /**
     * Adds to the blocking queue
     *
     * @param node Node to add
     */
    public void addToBQ(GraphNode node) {
        bq.add(node);
    }

    /**
     * Gets the message stored
     *
     * @return String of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the status of the packet
     *
     * @return true if successful
     */
    public boolean getStatus() {
        return success;
    }

    /**
     * Gets the sender of the packet by peeking into the BQ
     *
     * @return GraphNode of sender
     */
    public GraphNode getSender() {
        return bq.peek();
    }

    /**
     * Gets the Id of the packet
     * Currently random, but can be changed to something more useful later on
     *
     * @return Integer value of the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * Set the packet to be finished
     */
    public void setFinished() {
        msg = false;
        success = true;
    }

    /**
     * Gets the last node and returns it
     *
     * @return Last node
     */
    public GraphNode getLast() {
        return bq.removeLast();
    }

    /**
     * Returns an overwritten version of to String
     *
     * @return Better string representation
     */
    @Override
    public String toString() {
        return message + " " + getID() + " " + getStatus();
    }

    /**
     * Check if the GraphNode specified is held within BQ
     *
     * @param graphNode Graphnode to check
     * @return true if contained in bq
     */
    public boolean contains(GraphNode graphNode) {
        return bq.contains(graphNode);
    }

    /**
     * Set the packet to be failed
     */
    public void setFail() {
        success = false;
        msg = false;
    }

    /**
     * Checks if the packet is a message or a receipt
     *
     * @return boolean, true if message, false if receipt
     */
    public boolean isMessage() {
        return msg;
    }

}
