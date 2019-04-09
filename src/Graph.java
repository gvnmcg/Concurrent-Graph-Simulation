import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Graph Data structure for Mobile Agents
 */
public class Graph {

    // TODO Need to update this to something like a map, where it maps the coordinates to the node
    private HashMap<Coordinate, GraphNode> nodes = new HashMap<>();
    private LinkedList<Thread> nodeThreads = new LinkedList<>();
    private LinkedList<GraphEdge> edges = new LinkedList<>();

    //??
    private GraphNode baseStation;

//    private BaseStation station;


    /**
     * Graph constructor that creates a graph from the given text file
     * @param filename
     */
    Graph(String filename){
        readIn(filename);
    }

    private void readIn(String filename){
        BufferedReader in = null;

        ArrayList<String> lines = new ArrayList<>();
        int x, y;
        try {

            // Create buffered reader
            in = new BufferedReader(new FileReader(filename + ".txt"));


            // Lines adding
            String l;
            while ((l = in.readLine()) != null) lines.add(l);

            lines.sort((o1, o2) -> {
                char c1 = o1.charAt(0);
                char c2 = o2.charAt(0);

                if (c1 == 'n' || c2 == 'f' || c2 == 's') return -1;
                else if (c2 == 'n' || c1 == 'f' || c1 == 's') return 1;
                else return 0;
            });

            //read in the graph nodes, edges, base station, fire
            for (String line : lines) {
                String[] strArray = line.split(" ");

                switch (strArray[0]) {

                    //new node
                    case "node":

                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);

                        Coordinate coord = new Coordinate(x,y);
                        GraphNode node = new GraphNode(coord);

                        //save in data struct
                        nodes.put(coord, node);

                        nodeThreads.add(new Thread(node));

                        break;

                    //new edge
                    case "edge":

                        //break input into coordinates
                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);
                        Coordinate c1 = new Coordinate(x,y);

                        x = Integer.parseInt(strArray[3]);
                        y = Integer.parseInt(strArray[4]);
                        Coordinate c2 = new Coordinate(x,y);

                        //if the node hasnt been read in for whatever reason
                        // TODO Not sure if we can assume that the edges are as it mentions, w/o having corresponding node in file
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
                    case "station":

                        x = Integer.parseInt(strArray[1]);
                        y = Integer.parseInt(strArray[2]);
                        Coordinate b = new Coordinate(x,y);

//                        baseStation = nodes.get(c3);

                        //BaseStation has ref to the node
//                        station = new BaseStation(nodes.get(b));
                        baseStation = nodes.get(b);
                        nodes.get(b).setBase();

                        break;
                    case "fire":

                        Coordinate c = new Coordinate(strArray[1] + " " + strArray[2]);

                        //immediately set node to red

                        if (nodes != null && nodes.get(c) != null) {
                            System.out.println(nodes.get(c));
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

//        testGraph();

        //initialize statuses
        //for each node
        for (GraphNode node : nodes.values()) {

            //if node is on fire
            if (node.getStatus() == NodeStatus.RED) {

                //warn its adjacent nodes
                for (GraphNode yellowNodes : node.getAdjacentNodes()) {
                    yellowNodes.setStatus(NodeStatus.YELLOW);
                }
            }
        }
    }

    public void startThreads(GraphDisplay graphDisplay){

        for (Thread thr : nodeThreads) thr.start();
    }

    public Map<Coordinate, GraphNode> getNodes() {
        return nodes;
    }

    public LinkedList<GraphEdge> getEdges() {
        return edges;
    }

    public GraphNode getStation() {
        return baseStation;
    }


}
