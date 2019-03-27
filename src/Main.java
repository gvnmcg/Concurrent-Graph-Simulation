import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The Main Method of Mobile Agents
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

        //initial gragh data structure
        Graph graph = new Graph("resources/sample");
//        MobileAgent test = new MobileAgent(graph.getBaseStation(), true);

        //display graph
        GraphDisplay graphDisplay = new GraphDisplay(graph);
        MobileAgent.setGraphDisplay(graphDisplay);

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
