import java.util.concurrent.LinkedBlockingQueue;

public class Packet {

    String message;
    boolean update;
    boolean success;
    boolean inProgress;
    LinkedBlockingQueue<GraphNode> bq;

    Packet (String message, boolean update, LinkedBlockingQueue<GraphNode> bq) {
        success = false;
        this.message = message;
        this.update = update;
        this.bq = bq;
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

}
