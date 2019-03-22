import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        Graph graph = new Graph("resources/graph1");
        GraphDisplay graphDisplay = new GraphDisplay(graph);

        primaryStage.setScene(new Scene(graphDisplay.getRoot(), 700, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
	// write your code here
        launch(args);
    }
}
