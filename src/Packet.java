import java.util.concurrent.LinkedBlockingQueue;

public class Packet {

    String message;
    boolean update;
    boolean success;
    boolean inProgress;
    LinkedBlockingQueue<GraphNode> bq;
    int ID;

    Packet (String message, boolean update, GraphNode gn, int ID) {
        success = false;
        this.message = message;
        this.update = update;
        bq.add(gn);
        this.ID = ID;

    }

    public void addToBQ(GraphNode node) {
        bq.add(node);
    }

    public void removeFromBQ(GraphNode node) {
        bq.remove(node);
    }

    public String getMessage() {
        return message;
    }

    public boolean getStatus() {
        return success;
    }

    public GraphNode getSender() {
        return bq.peek();
    }

    public int getID() {
        return ID;
    }
}
