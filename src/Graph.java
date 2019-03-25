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
//    private Map<String, GraphNode> nodes = new HashMap<>();
    private HashMap<Coordinate, GraphNode> nodes = new HashMap<>();
    private LinkedList<Thread> nodeThreads = new LinkedList<>();
    private LinkedList<GraphEdge> edges = new LinkedList<>();
    private GraphNode baseStation;

    /**
     * Graph constructor that creates a graph from the given text file
     * @param filename
     */
    Graph(String filename){

//        Scanner sc = new Scanner(new File(filename + ".txt"));

        BufferedReader in = null;
        String line;

        int x, y;
        try {
            in = new BufferedReader(new FileReader(filename + ".txt"));

//          Not finished but plan to finish this up
            while ((line = in.readLine()) != null) {
                String[] strArray = line.split(" ");


                switch (strArray[0]) {
                    case "node":

                        //new node
                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);

                        Coordinate coord = new Coordinate(x,y);
                        GraphNode node = new GraphNode(coord);

                        //save in data struct
                        nodes.put(coord, node);

                        nodeThreads.add(new Thread(node));

                        break;
                    case "edge":

                        //break input into coordinates
                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);
                        Coordinate c1 = new Coordinate(x,y);

                        x = Integer.parseInt(strArray[3]);
                        y = Integer.parseInt(strArray[4]);
                        Coordinate c2 = new Coordinate(x,y);


                        //FIXED Had to overwrite the hashcode function within the coordinate class to make sure equality is checked based on how you want it for more proprietary classes
                        if (nodes.containsKey(c1) && nodes.containsKey(c2)){
                            edges.add(new GraphEdge(nodes.get(c1), nodes.get(c2)));
                        }


                        if (nodes.get(c1) != null && nodes.get(c2) != null) {
                            nodes.get(c1).addEdge(nodes.get(c2));
                        }

                        break;
                    case "station":

                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);
                        Coordinate c3 = new Coordinate(x,y);

                        baseStation = new GraphNode(c3);
                        break;
                    case "fire":
//                        String c = strArray[1] + " " + strArray[2];
                        Coordinate c = new Coordinate(strArray[1] + " " + strArray[2]);

                        if (nodes != null && nodes.get(c) != null) {
                            System.out.println(nodes.get(c));
                            nodes.get(c).setStatus(NodeStatus.RED);
                            System.out.println(nodes.get(c).toString() + "Status: RED");
                        }
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        System.out.println(nodes);
//        System.out.println(edges);

        for (GraphNode node : nodes.values()) {
            System.out.println(node);
            System.out.println(node.getAdjacentNodes().size());
            node.printNeighbors();

        }


        MobileAgent test = new MobileAgent(getBaseStation(), true);
        for (Thread thr : nodeThreads) thr.start();

//        testGraph();
    }



    public Map<Coordinate, GraphNode> getNodes() {
        return nodes;
    }

    public LinkedList<GraphEdge> getEdges() {
        return edges;
    }

    public GraphNode getBaseStation() {
        return baseStation;
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
