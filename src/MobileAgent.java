import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MobileAgent implements Runnable {

    // TODO Get better way to display the mobile agent
    private static GraphDisplay gd = null;
    private GraphNode node;
    private Thread thread;
    private Circle display;

    /**
     * sets the Graph Display and calls other constructor
     * @param node
     * @param gd
     * @param init
     */
    MobileAgent(GraphNode node, GraphDisplay gd, boolean init) {
        this(node, init);
        singletonGD(gd);
        initDisplay();
    }

    /**
     * Set singleton GD for Mobile Agent
     * @param gd
     */
    private void singletonGD(GraphDisplay gd) {
        if (this.gd == null) this.gd = gd;
    }

    /**
     * Sets assigned node
     * Sets this agents to node
     *
     * If this is the base Station, start walk to fire
     * @param node
     * @param init
     */
    MobileAgent(GraphNode node, boolean init) {
        if (Main.debugMobileAgents) System.out.println("MA Initialized @ " + node);
        initDisplay();

        this.node = node;
        if (init) {
            walkToFire(node);
        } else {
            // Propogated
            this.node = node;
        }

        this.node.setMobileAgent(this);
        thread = new Thread(this);
        thread.start();
    }

    /**
     * random walk
     * @param node
     * @return
     */
    private GraphNode walkToFire(GraphNode node) {

        // If current node is on fire then isolate and propagate
        if (Main.debugMobileAgents) System.out.println("Walking node: " + node );
        if (node.getStatus() == NodeStatus.YELLOW) {
            if (Main.debugMobileAgents) System.out.println("Yellow at " + node);
            this.node = node;
            return node;
        }
        // Else randomly walk to an adjacent node
        else {
            int random = (int)(Math.random() * node.getAdjacentNodes().size());
            return walkToFire(node.getAdjacentNodes().get(random));
        }
    }


    private void randomWalk() {

        while(node.getStatus() == NodeStatus.GREEN){

            node = node.getAdjacentNodes().get(
                    (int)(Math.random()*node.getAdjacentNodes().size()));

            updateDisplay(node.getCoordinate());

//            Thread.sleep( 2000);

        }

        updateDisplay(node.getCoordinate());

    }

    private void updateDisplay(Coordinate coordinate) {
        if (display != null) {
            synchronized (display) {
                if (Main.debugMobileAgents) System.out.println("Updating display of " + node);
                display.setCenterX(coordinate.getX() * GraphDisplay.scale);
                display.setCenterY(coordinate.getY() * GraphDisplay.scale);
                switch (node.getStatus()) {
                    case GREEN:
                        display.setFill(Color.WHITE);
                        break;
                    case YELLOW:
                        display.setFill(Color.ORANGE);
                        break;
                    case RED:
                        display.setFill(Color.BLACK);
                        break;
                }
            }
        }
    }

    /**
     *clone
     *
     */
    private void propagate() {
        if (Main.debugMobileAgents) System.out.println("Propagating nodes @ " + node + " of size " + node.getAdjacentNodes().size());

        for (GraphNode n : this.node.getAdjacentNodes()) {
            if (Main.debugMobileAgents) System.out.println("Propagating to " + n + " with status " + n.getStatus());
            synchronized (n) {
                if (n.getStatus() != NodeStatus.RED && n.getMobileAgent() == null) new MobileAgent(n, false);
            }
        }
    }

    public void notify(NodeStatus status) {

    }

    @Override
    public void run() {

        updateDisplay(node.getCoordinate());
        if (Main.debugMobileAgents) System.out.println("MA: " + node + " | Status: " + node.getStatus());
        node.addPacket(new Packet("MA: " + node + " | Status: " + node.getStatus(), false, node, (int)(Math.random()*20000)));
        while (node.getStatus() == NodeStatus.GREEN) {
            try {
                synchronized (this) {  wait(); }
            } catch (InterruptedException e) {
                if (Main.debugMobileAgents) System.out.println("It broke");
                e.printStackTrace();
            }
        }

        updateDisplay(node.getCoordinate());
        // While there is an adjacent fire
        if (Main.debugMobileAgents) System.out.println("MA: " + node + " | Status: " + node.getStatus());
        node.addPacket(new Packet("MA: " + node + " | Status: " + node.getStatus(), false, node, (int)(Math.random()*20000)));
        propagate();
        while (node.getStatus() == NodeStatus.YELLOW) {
            try {
                synchronized (this) {  wait(); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (Main.debugMobileAgents) System.out.println(node + " " + display.getFill() + " " + node.getStatus());
        updateDisplay(node.getCoordinate());
        if (Main.debugMobileAgents) System.out.println("MA: " + node + " | Status: " + node.getStatus());
        node.addPacket(new Packet("MA: " + node + " | Status: " + node.getStatus(), false, node, (int)(Math.random()*20000)));
    }


    public Circle getDisplay() {
        return display;
    }

    public void initDisplay() {
            if (gd != null) {
                Circle c = new Circle(5);
                if (node != null) {
                    c.setCenterX(node.getCoordinate().getX());
                    c.setCenterY(node.getCoordinate().getY());
                }
                c.setFill(Color.GREEN);

                this.display = c;
                gd.addToCenter(display);

                if (Main.debugMobileAgents) System.out.println("Added to display");
            }

    }
}
