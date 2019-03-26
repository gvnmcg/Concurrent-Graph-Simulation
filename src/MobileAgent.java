import java.util.Iterator;

public class MobileAgent implements Runnable {

    private GraphNode node;
    private NodeStatus status;

    MobileAgent(GraphNode node, boolean init) {
        System.out.println(node.getAdjacentNodes().size());
        if (init) {
            walkToFire(node);
        } else {
            // Propogated
            this.node = node;
        }

        this.node.setMobileAgent(this);
        new Thread(this).start();
    }

    private GraphNode walkToFire(GraphNode node) {
        System.out.println("Walking node: " + node );
        if (node.getStatus() == NodeStatus.YELLOW) {
            System.out.println("Yellow at " + node);
            this.node = node;
            return node;
        }
        else {
            int random = (int)(Math.random() * node.getAdjacentNodes().size());
            return walkToFire(node.getAdjacentNodes().get(random));
        }

    }

    private void propagate() {
        for (GraphNode node : this.node.getAdjacentNodes())
            if (node.getStatus() == NodeStatus.GREEN) new MobileAgent(node, false);
    }

    public void notify(NodeStatus status) {

    }

    @Override
    public void run() {
        System.out.println("MA: " + node + " | Status: " + node.getStatus());
        while (node.getStatus() == NodeStatus.GREEN) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
