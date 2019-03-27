import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Iterator;
import java.util.LinkedList;

public class MobileAgent implements Runnable {

    static int count;
    private int id;

    private LinkedList<Coordinate> route = new LinkedList<>();

    private GraphNode node;
    private NodeStatus status;
    private Thread thread;

    private Circle display;

    MobileAgent(GraphNode node, boolean init) {
        System.out.println(node.getAdjacentNodes().size());

        this.node = node;
        initDisplay();
//        if (init) {
//            walkToFire(node);
//        } else {
//            // Propogated
//            this.node = node;
//        }

//        this.node.setMobileAgent(this);
//        thread = new Thread(this);
//        thread.start();
    }

    /**
     * random walk
     * @param node
     * @return
     */
    private GraphNode walkToFire(GraphNode node) {

        //if current node is on fire then isolate and propagate
        System.out.println("Walking node: " + node );
        if (node.getStatus() == NodeStatus.YELLOW) {
            System.out.println("Yellow at " + node);
            this.node = node;
            return node;
        }
        //else randomly walk to an adjacent node
        else {
            int random = (int)(Math.random() * node.getAdjacentNodes().size());
            return walkToFire(node.getAdjacentNodes().get(random));
        }

    }

    private void randomWalk() {

        //random walk until node catches fire
        while(node.getStatus() == NodeStatus.GREEN){

            //add coordinates on route
            route.add(node.getCoordinate());

            //change node reference
            node = node.getAdjacentNodes().get(
                    (int)(Math.random()*node.getAdjacentNodes().size()));

            //updates the display with node location
            updateDisplay(node.getCoordinate());

            System.out.println("MA on node  " + node);

            try {
                Thread.sleep( 2000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

        }

        updateDisplay(node.getCoordinate());
    }

    private void updateDisplay(Coordinate coordinate) {

        display.setCenterX(coordinate.getX() * GraphDisplay.scale);
        display.setCenterY(coordinate.getY() * GraphDisplay.scale);

    }

    /**
     *clone
     *
     */
    private void propagate() {
        System.out.println("Propagating nodes @ " + node);
        for (GraphNode node : this.node.getAdjacentNodes()){

            if (node.getStatus() == NodeStatus.GREEN){
                MobileAgent ma = new MobileAgent(node, false);
            }



//            if (node.getStatus() == NodeStatus.GREEN) new MobileAgent(node, false);

        }
    }

    public void notify(NodeStatus status) {

    }

    /**
     * the mobile agent
     * - randomly walks until it step so on a YELLOW node
     * - waits and makes the node wait preventing the the node from catching fire
     */
    @Override
    public void run() {


        //random walk until node catches fire
        randomWalk();

        /*TODO
        alright sorry If I moved to many  things around
        trying to understand how this works

        i got the dot to move around
        it does not stop  the fire but ill continue to work on it
        until I go to sleep
         */

        System.out.println("MA: " + node + " | Status: " + node.getStatus());
        while (node.getStatus() == NodeStatus.GREEN) {
            try {
                synchronized (this) {  wait(); }
            } catch (InterruptedException e) {
                System.out.println("It broke");
                e.printStackTrace();
            }
        }

        //while there is an adjacent fire
        System.out.println("MA: " + node + " | Status: " + node.getStatus());
        //clone
        propagate();
        while (node.getStatus() == NodeStatus.YELLOW) {
            try {
                synchronized (this) {  wait(); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("MA: " + node + " | Status: " + node.getStatus());
    }

    public static void setGraphDisplay(GraphDisplay graphDisplay) {
//        MobileAgent.graphDisplay = graphDisplay;
    }

    public Circle getDisplay() {
        return display;
    }

    public void initDisplay() {

        Circle c = new Circle(20);
        c.setCenterX(node.getCoordinate().getX());
        c.setCenterY(node.getCoordinate().getY());
        c.setStroke(Color.GREEN);
        c.setStrokeWidth(5);
        c.setFill(Color.TRANSPARENT);

        this.display = c;
    }
}
