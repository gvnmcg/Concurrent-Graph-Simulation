import java.util.concurrent.LinkedBlockingDeque;

public class Packet {

    String message;
//    boolean update;
    boolean success;
    boolean msg;
    LinkedBlockingDeque<GraphNode> bq = new LinkedBlockingDeque<>();
    int ID;

    Packet (String message, boolean update, GraphNode gn, int ID) {
        success = false;
        msg = true;
        this.message = message;
//        this.update = update;
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

    public void setFinished() {
        msg = false;
        success = true;
    }


    public GraphNode getLast() {
        return bq.removeLast();
}

    @Override
    public String toString() {
        return message + " " + getID() + " " + getStatus();
    }

    public boolean contains(GraphNode graphNode) {
        return bq.contains(graphNode);
    }

    public void printBQ() {
        if (Main.debugMessaging) {
            for (GraphNode node : bq)
                System.out.print(node + " | ");
            System.out.println();
        }
    }

    public void setFail() {
        success = false;
//        msg = true;
    }

    public boolean isMessage() {
        return msg;
    }
}
