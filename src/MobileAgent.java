import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Mobile agent class
 */
public class MobileAgent implements Runnable {

    // Private variables for the class
    private static GraphDisplay gd = null;
    private GraphNode node;
    private Thread thread;
    private Circle display;


    /**
     * sets the Graph Display and calls other constructor
     * @param node GraphNode attached to
     * @param gd GD to update to
     * @param init true if walk is required, false if not
     */
    MobileAgent(GraphNode node, GraphDisplay gd, boolean init) {
        this(node, init);
        singletonGD(gd);

        if (init) initDisplay();
    }

    /**
     * Set singleton GD for Mobile Agent
     * @param gd Sets graphic display
     */
    private void singletonGD(GraphDisplay gd) {
        if (this.gd == null) this.gd = gd;
    }

    /**
     * Sets assigned node
     * Sets this agents to node
     *
     * If this is the base Station, start walk to fire
     * @param node GraphNode attached to
     * @param init true if walk is required
     */
    MobileAgent(GraphNode node, boolean init) {
        initDisplay();

        // Sets the node and walks
        this.node = node;
        if (init) {
            walkToFire(node);
        }

        // Sets thread data
        this.node.setMobileAgent(this);
        thread = new Thread(this);
        thread.start();
    }

    /**
     * Random walk to a yellow node
     * Will find it or bust
     *
     * @param node Current GraphNode of the crawl
     * @return Graphnode of yellow node
     */
    private GraphNode walkToFire(GraphNode node) {
        // If current node is on fire then isolate and propagate
        if (node.getStatus() == NodeStatus.YELLOW) {
            // navigate
            this.node = node;
            return node;
        }
        // Else randomly walk to an adjacent node
        else {
            int random = (int)(Math.random() * node.getAdjacentNodes().size());
            GraphNode randNode = node.getAdjacentNodes().get(random);

            // Walk to the node if it is not on fire
            if (randNode.getStatus() != NodeStatus.RED) {
                return walkToFire(node.getAdjacentNodes().get(random));
            } else return walkToFire(node);
        }
    }

    /**
     * Updates the display of
     * @param coordinate
     */
    private void updateDisplay(Coordinate coordinate) {
        if (display != null) {
            display.setCenterX(coordinate.getX() * GraphDisplay.scale);
            display.setCenterY(coordinate.getY() * GraphDisplay.scale);
            Platform.runLater(() -> {
                switch (node.getStatus()) {
                    case GREEN:
                        display.setStroke(Color.DARKBLUE);
                        break;
                    case YELLOW:
                        display.setStroke(Color.DARKORANGE);
                        break;
                    case RED:
                        display.setStroke(Color.CRIMSON);
                        break;
                }
            });
        }
    }

    /**
     * Clone the mobile agents to other nodes
     */
    private void propagate() {
        // Loop through nodes and add mobile agents
        for (GraphNode n : this.node.getAdjacentNodes()) {
            synchronized (n) {
                if (n.getStatus()!=NodeStatus.RED && n.getMobileAgent()==null){
                    new MobileAgent(n, false);
                }
            }
        }
    }

    /**
     * Run and match actions specified in the PDF
     */
    @Override
    public void run() {

        // Run functions emulating the behavior of the node
        onBlueNode();
        onYellowNode();

        updateDisplay(node.getCoordinate());
        // Send final node
        node.addPacket(new Packet(
                "MA: " + node + " | Status: " + node.getStatus(),
                false, node, (int)(Math.random()*20000)));

        synchronized (node) { node.notify(); }
        // Dies here with the GraphNode
    }

    /**
     * Emulate the behavior of a blue node
     */
    private void onBlueNode() {

        // Update the display
        updateDisplay(node.getCoordinate());

        // Send package if the node is green
        if (node.getStatus() == NodeStatus.GREEN) {
            node.addPacket(new Packet(
                    "MA: " + node + " | Status: " + node.getStatus(),
                    false, node, (int)(Math.random()*20000)));
        }

        // Wait until it is time to change
        while (node.getStatus() == NodeStatus.GREEN) {
            try {
                synchronized (this) {  wait(); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Update the display
        updateDisplay(node.getCoordinate());
    }

    /**
     * Emulates the behavior of a Yellow node
     */
    private void onYellowNode() {
        // Propagate/clone the yellow nodes
        propagate();

        // While there is an adjacent fire
        node.addPacket(new Packet(
                "MA: " + node + " | Status: " + node.getStatus(),
                false, node, (int)(Math.random()*20000)));

        // Wait until the node status turns RED
        while (node.getStatus() == NodeStatus.YELLOW) {
            try {
                synchronized (this) {  wait(); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Initializes the display of a Mobile agent
     */
    public void initDisplay() {
        if (gd != null) {
            // Set GUI components
            Circle c = new Circle(20);
            c.setStroke(Color.AZURE);
            c.setFill(Color.TRANSPARENT);
            c.setStrokeWidth(3);

            // Set the center to be the same as the node's
            if (node != null) {
                c.setCenterX(node.getCoordinate().getX());
                c.setCenterY(node.getCoordinate().getY());
                updateDisplay(node.getCoordinate());
            }

            // Update the display and apply it to the GUI
            this.display = c;
            Platform.runLater(() -> gd.addToCenter(display));
        }
    }
}
