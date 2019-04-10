import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Graph Data structure for Mobile Agents
 */
public class Graph {

    // Private variables where the abstract data structures are held
    private HashMap<Coordinate, GraphNode> nodes = new HashMap<>();
    private LinkedList<Thread> nodeThreads = new LinkedList<>();
    private LinkedList<GraphEdge> edges = new LinkedList<>();
    private GraphNode baseStation;


    /**
     * Graph constructor that creates a graph from the given text file
     * @param filename
     */
//    Graph(String filename){
//        readIn(filename);
//    }

    Graph(File file){
        readIn(file);
    }

    /**
     * Read in the file name with the given .txt component
     * @param file
     */
    private void readIn(File file){
        BufferedReader in;
        ArrayList<String> lines = new ArrayList<>();

        int x, y;
        try {

            // Create buffered reader
            in = new BufferedReader(new FileReader (file));


            // Adds the users
            String l;
            while ((l = in.readLine()) != null) lines.add(l);

            // Sorts the lines based on the first character
            lines.sort((o1, o2) -> {
                char c1 = o1.charAt(0);
                char c2 = o2.charAt(0);

                // Run various if statements to reorder the lines
                if (c1 == 'n' || c2 == 'f' || c2 == 's') return -1;
                else if (c2 == 'n' || c1 == 'f' || c1 == 's') return 1;
                else return 0;
            });

            // Read in the graph nodes, edges, base station, fire
            for (String line : lines) {

                // Split based on the spaces
                String[] strArray = line.split(" ");

                switch (strArray[0]) {

                    // Create a new Node
                    case "node":

                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);

                        Coordinate coord = new Coordinate(x,y);
                        GraphNode node = new GraphNode(coord);

                        // Create and save in the data structure
                        nodes.put(coord, node);
                        nodeThreads.add(new Thread(node));

                        break;

                    // New edge
                    case "edge":

                        //break input into coordinates
                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);
                        Coordinate c1 = new Coordinate(x,y);

                        x = Integer.parseInt(strArray[3]);
                        y = Integer.parseInt(strArray[4]);
                        Coordinate c2 = new Coordinate(x,y);

                        // If the node hasnt been read in for whatever reason
                        if (!nodes.containsKey(c2)){
                            node = new GraphNode(c2);
                            nodes.put(c2, node);
                        }
                        if (!nodes.containsKey(c1)){
                            node = new GraphNode(c1);
                            nodes.put(c1, node);
                        }

                        //if the nodes has been read in
                        if (nodes.containsKey(c1) && nodes.containsKey(c2)){
                            edges.add(new GraphEdge(nodes.get(c1), nodes.get(c2)));
                        }


                        if (nodes.get(c1) != null && nodes.get(c2) != null) {
                            nodes.get(c1).addEdge(nodes.get(c2));
                        }

                        break;
                    // Set the station
                    case "station":

                        // Set the base station!
                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);
                        Coordinate b = new Coordinate(x,y);

                        baseStation = nodes.get(b);
                        nodes.get(b).setBase();

                        break;
                    // Set the node that was on fire
                    case "fire":

                        Coordinate c = new Coordinate(strArray[1] + " " + strArray[2]);

                        // Immediately set node to red
                        if (nodes != null && nodes.get(c) != null) {
                            nodes.get(c).setStatus(NodeStatus.RED);
                        }

                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        // Initializes status for the corresponding nodes
        for (GraphNode node : nodes.values()) {

            // Set the fire node's neighbors to be yellow
            if (node.getStatus() == NodeStatus.RED) {

                // Set the adjacent node's statuses
                for (GraphNode yellowNodes : node.getAdjacentNodes()) {
                    yellowNodes.setStatus(NodeStatus.YELLOW);
                }
            }
        }
    }

    /**
     * Start the threads
     */
    public void startThreads(){
        for (Thread thr : nodeThreads) thr.start();
    }

    /**
     * Get the mapped nodes
     *
     * @return Map of Coordinates to the Nodes
     */
    public Map<Coordinate, GraphNode> getNodes() {
        return nodes;
    }

    /**
     * Get the edges of the Graph for GUI drawing purposes
     *
     * @return List of Edges
     */
    public LinkedList<GraphEdge> getEdges() {
        return edges;
    }

    /**
     * Get the station of the GraphNode
     *
     * @return GraphNode of station
     */
    public GraphNode getStation() {
        return baseStation;
    }


}
