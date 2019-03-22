import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Graph {

    // TODO Need to update this to something like a map, where it maps the coordinates to the node
    // This will better allow adding edges and traversals
//    private LinkedList<GraphNode> nodes = new LinkedList<>();
    private Map<String, GraphNode> nodes = new HashMap<>();
    private LinkedList<Thread> nodeThreads = new LinkedList<>();

    /**
     * Graph constructor that creates a graph from the given text file
     * @param filename
     */
    Graph(String filename){

//        Scanner sc = new Scanner(new File(filename + ".txt"));

        BufferedReader in = null;
        String line;

        int x, y;
        int scale = 30;
        try {
            in = new BufferedReader(new FileReader(filename + ".txt"));

//          Not finished but plan to finish this up
            while ((line = in.readLine()) != null) {
                String[] strArray = line.split(" ");
                switch (strArray[0]) {
                    case "node":
                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);

                        GraphNode node = new GraphNode(x,y);
                        nodes.put("" + x + " " + y, node);
                        nodeThreads.add(new Thread(node));

                        break;
                    case "edge":
                        String c1 = strArray[1] + " " + strArray[2];
                        String c2 = strArray[3] + " " + strArray[4];

                        if (nodes.get(c1) != null && nodes.get(c2) != null)
                            nodes.get(c1).addEdge(nodes.get(c2));

                        break;
                    case "station":


                        break;
                    case "fire":
                        String c = strArray[1] + " " + strArray[2];

                        if (nodes.get(c) != null) {
                            nodes.get(c).setStatus(NodeStatus.RED);
                        }
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Thread thr : nodeThreads) thr.start();

//        testGraph();
    }

    public Map<String, GraphNode> getNodes() {
        return nodes;
    }

    private void testGraph() {
        //        Some tests for GraphNodes
        GraphNode gn1 = new GraphNode(0,0);
        GraphNode gn2 = new GraphNode(2,0);
        GraphNode gn3 = new GraphNode(0,2);

        gn1.addEdge(gn2);
        gn3.addEdge(gn1);
        gn3.addEdge(gn2);

        gn1.printNeighbors();
        gn2.printNeighbors();
        gn3.printNeighbors();


        gn1.setStatus(NodeStatus.YELLOW);

        Thread t1 = new Thread(gn1);
        t1.start();
        Thread t2 = new Thread(gn2);
        t2.start();
        Thread t3 = new Thread(gn3);
        t3.start();
    }
}
