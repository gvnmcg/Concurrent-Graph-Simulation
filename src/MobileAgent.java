import java.util.Iterator;

public class MobileAgent implements Runnable {

    private GraphNode node;
    private NodeStatus status;
    private Thread thread;

    MobileAgent(GraphNode node, boolean init) {
        System.out.println(node.getAdjacentNodes().size());
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

        while(node.getStatus() == NodeStatus.GREEN){

            node = node.getAdjacentNodes().get(
                    (int)(Math.random()*node.getAdjacentNodes().size()));

        }
    }

    /**
     *clone
     *
     */
    private void propagate() {
        for (GraphNode node : this.node.getAdjacentNodes())
            if (node.getStatus() == NodeStatus.GREEN) new MobileAgent(node, false);
    }

    public void notify(NodeStatus status) {

    }

    @Override
    public void run() {


        //while the agents node is fine
//        while(node.getStatus() == NodeStatus.GREEN){
//            randomWalk();
//        }

        System.out.println("MA: " + node + " | Status: " + node.getStatus());
        while (node.getStatus() == NodeStatus.GREEN) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //while there is an adjacent fire
        System.out.println("MA: " + node + " | Status: " + node.getStatus());
        propagate();
        while (node.getStatus() == NodeStatus.YELLOW) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("MA: " + node + " | Status: " + node.getStatus());
    }




}
