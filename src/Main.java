import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        Graph graph = new Graph("resources/graph");
        MobileAgent test = new MobileAgent(graph.getBaseStation(), true);
        GraphDisplay graphDisplay = new GraphDisplay(graph);


        //does not work
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
            primaryStage.close();
        });


        primaryStage.setScene(new Scene(graphDisplay.getRoot(), 700, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
	// write your code here
        launch(args);
    }
}
