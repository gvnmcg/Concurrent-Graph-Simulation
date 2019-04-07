import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.GridPaneBuilder;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The Main Class of Mobile Agents
 */
public class Main extends Application {

    public static boolean debugMessaging = false;
    public static boolean debugMobileAgents = false;
    public static boolean debugGraphNode = false;

    String fileSelction = "sample";

    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;

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
        Graph graph = new Graph("resources/graphgen");

        // Display graph
        GraphDisplay graphDisplay = new GraphDisplay(graph);

        MobileAgent test = new MobileAgent(graph.getStation(), graphDisplay,  true);
        // Start simulation
        graph.startThreads(graphDisplay);


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
