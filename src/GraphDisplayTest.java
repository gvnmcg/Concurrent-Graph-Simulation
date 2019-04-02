import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GraphDisplayTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Initialize gragh data structure
        Graph graph = new Graph("resources/TestForLock");

        // Display graph
//        GraphDisplay graphDisplay = new GraphDisplay(graph);
        GraphDisplay graphDisplay = new GraphDisplay(graph, true);
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
