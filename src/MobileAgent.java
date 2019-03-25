public class MobileAgent implements Runnable {

    private GraphNode node;

    MobileAgent(GraphNode node, boolean init) {
        System.out.println(node.getAdjacentNodes().size());
        if (init) {
            walkToFire(node);

        } else {



        }
        new Thread(this).start();
    }

    private GraphNode walkToFire(GraphNode node) {
            System.out.println(node);
            node.printNeighbors();
            System.out.println();
        if (node.getStatus() == NodeStatus.YELLOW ) {
            this.node = node;
            return node;
        } else if (node.getStatus() == NodeStatus.RED) {
            return null;
        } else {
            int random = (int)(Math.random() * node.getAdjacentNodes().size());
            return walkToFire(node.getAdjacentNodes().get(random));
        }

    }

    @Override
    public void run() {

    }
}
