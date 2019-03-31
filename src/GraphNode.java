import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Graph Node for Graph data structure
 */
public class GraphNode implements Runnable {

    private ArrayList<GraphNode> adjacentNodes = new ArrayList<>();
    private Coordinate cords;
    private NodeStatus status;
    private Circle display;
    private MobileAgent mobileAgent;
    boolean base = false;
    LinkedBlockingQueue<Packet> mailbox = new LinkedBlockingQueue<>();


    GraphNode(Coordinate coordinate) {
        display = new Circle(10);
        cords = coordinate;
        this.status = NodeStatus.GREEN;
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
        Platform.runLater(() -> {
            synchronized (display) {
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
        synchronized (this.status) {
            this.status = status;
        }
    }

    public Coordinate getCoordinate() {
        return cords;
    }


    public Circle getDisplay() {
        synchronized(status) {
            display.setCenterX(getCoordinate().getX() * GraphDisplay.scale);
            display.setCenterY(getCoordinate().getY() * GraphDisplay.scale);
            return display;
        }
    }

    public boolean sendMessage(Packet p) {
        if (base == true) {
            System.out.println(p.getMessage());
            return true;
        }

        p.addToBQ(this);
        for (GraphNode node : adjacentNodes) {
            if (node.getStatus() != NodeStatus.RED) {
                node.addPacket(p);
                node.notify();
                getReceipt(p.getID());
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

        //if node is notified of a fire
        if (getStatus() == NodeStatus.YELLOW) {
            System.out.println("Node: " + toString() + "; Status: " + status + " 2");

            //wait to catch on fire
            try {
                Thread.yield();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("Tried sleeping!");
                e.printStackTrace();
            }
        }


        //on fire
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
}
//            // Check status of other nodes
//            synchronized (adjacentNodes) {
//
//                //check adjecent nodes for fires
//                for (GraphNode node : adjacentNodes) {
//
//                    //if it detects a fire then set to YELLOW
//                    if (node.getStatus() == NodeStatus.RED) {
//                        System.out.println(toString() + " found out that it's neighbor " + node.toString() + " is on fire!");
//                        setStatus(NodeStatus.YELLOW);
//
//                        //if there is a agent, notify the agent
//                        if (mobileAgent != null) {
//                            synchronized (mobileAgent) {
//                                System.out.println("Notifying MA");
////                                mobileAgent.notify(getStatus());
//                                mobileAgent.notify();
//                            }
//                        }
//                        break;
//                    }
//                }
//            }
