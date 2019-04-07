import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;

public class GraphDisplayTest extends Application {

    public static boolean debugMessaging = false;
    public static boolean debugMobileAgents = false;
    public static boolean debugGraphNode = false;

    String fileSelction = "sample";

    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;


    @Override
    public void start(Stage primaryStage) throws Exception {

        // Initialize gragh data structure
        Graph graph = new Graph("resources/TestForLock");

        // Display graph
        GraphDisplay graphDisplay = new GraphDisplay(graph);
//        GraphDisplay graphDisplay = new GraphDisplay(graph, true);
        graph.startThreads(graphDisplay);


        // Does not work???
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
            primaryStage.close();
        });


//        primaryStage.setScene(new Scene(graphDisplay.getRoot(), 700, 500));
        primaryStage.setScene(introScene());
        primaryStage.show();
    }


    private void initGraph(String filename, Stage primaryStage){
        // Initialize gragh data structure
        Graph graph = new Graph("resources/fireNextToBaseStation");

        // Display graph
        GraphDisplay graphDisplay = new GraphDisplay(graph);

        MobileAgent test = new MobileAgent(graph.getStation(), graphDisplay,  true);
        // Start simulation
        graph.startThreads(graphDisplay);
    }

    private Scene introScene(){

//        BorderPane introRoot = new BorderPane();
        GridPane introRoot = new GridPane();

        Text title = new Text("MobileAgents");

        ScrollPane graphselectionPane = graphSelectionScrollPane();

        Button startButton = new Button("Start");
        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,handleConfirmation());

        Button infoBoxButton = new Button("what's this?");
        infoBoxButton.addEventHandler(MouseEvent.MOUSE_CLICKED,handleInfoBox());

        introRoot.add(title, 1,1);
        introRoot.add(graphselectionPane, 1, 2);
        introRoot.add(startButton, 1,2);
        introRoot.add(infoBoxButton, 2,2);

        return new Scene(introRoot, WIDTH, HEIGHT);
    }

    private EventHandler<MouseEvent> handleInfoBox() {

        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        };
    }

    private ScrollPane graphSelectionScrollPane() {

        ScrollPane scrollPane = new ScrollPane();


        ListView<String> list = new ListView<String>();

        ArrayList<String> filenamesList = new ArrayList<>();

        File folder = new File("resources");
        for (String file : folder.list()){
            filenamesList.add(file.substring(0,file.length() - 4));
        }

        ObservableList<String> items = FXCollections.observableArrayList (filenamesList);




        list.setItems(items);

        scrollPane.setContent(list);
        scrollPane.setMaxWidth(100);
        scrollPane.setMaxWidth(100);

        return scrollPane;
    }

    EventHandler<MouseEvent> handleStart(Graph graph,GraphDisplay graphDisplay ){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                graph.startThreads(graphDisplay);
            }
        };
    }

    EventHandler<MouseEvent> handleReload(){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {


            }
        };
    }

    EventHandler<MouseEvent> handleConfirmation(){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {


            }
        };
    }

    EventHandler<MouseEvent> handleGraphSelection(){
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        };
    }

    public static void main(String[] args) {

        launch(args);
    }
}
