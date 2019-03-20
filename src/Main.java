import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane bp = new BorderPane();

        Graph g = new Graph("");

        primaryStage.setScene(new Scene(bp, 300, 400));
    }

    public static void main(String[] args) {
	// write your code here
        launch(args);
    }
}
