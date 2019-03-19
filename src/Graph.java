import com.company.GraphNode;

import java.io.File;
import java.util.LinkedList;
import java.util.Scanner;

public class Graph {

    LinkedList<GraphNode> nodes = new LinkedList<>();

    Graph(String filename){

        Scanner sc = new Scanner(new File(filename + ".txt"));

    }
}
