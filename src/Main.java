import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main Class of Mobile Agents
 */
public class Main extends Application {

    /**
     * start the basic components
     * - the graph data stucture
     * - the display for the graph
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        //initialize gragh data structure
        Graph graph = new Graph("resources/TestForLock");

        //display graph
        GraphDisplay graphDisplay = new GraphDisplay(graph);

        MobileAgent test = new MobileAgent(graph.getStation(), graphDisplay,  true);
        //start simulation
        graph.startThreads(graphDisplay);


        //does not work???
//        primaryStage.setOnCloseRequest(e -> {
//            e.consume();
//            primaryStage.close();
//        });


        primaryStage.setScene(new Scene(graphDisplay.getRoot(), 700, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
	// write your code here
        launch(args);
    }
}
