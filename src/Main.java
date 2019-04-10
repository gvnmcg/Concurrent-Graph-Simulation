import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

//import javafx.scene.layout.GridPaneBuilder;

/**
 * The Main Class of Mobile Agents
 */
public class Main extends Application {

    public static boolean debugMessaging = false;
    public static boolean debugMobileAgents = false;
    public static boolean debugGraphNode = false;
    String fileSelction = "sample";


    /**
     * Start the basic components
     * - The graph data stucture
     * - The display for the graph
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Initialize gragh data structure
        Graph graph = new Graph(new File("resources/Star"));

        // Display graph
        GraphDisplay graphDisplay = new GraphDisplay(graph);

        MobileAgent test = new MobileAgent(graph.getStation(), graphDisplay,  true);
        // Start simulation
        graph.startThreads();


        // Does not work???
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
            primaryStage.close();
        });


        primaryStage.setScene(new Scene(graphDisplay.getRoot(), 700, 500));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
