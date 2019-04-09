import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
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

    GraphDisplay graphDisplay;

    Stage window;

    String buttonStyle =  "    -fx-text-fill: #006464;\n" +
            "    -fx-background-color: #DFB951;\n" +
            "    -fx-border-radius: 20;\n" +
            "    -fx-background-radius: 20;\n" +
            "    -fx-padding: 5;";


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        // Does not work???
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
            primaryStage.close();
        });


//        primaryStage.setScene(new Scene(graphDisplay.getRoot(), 700, 500));
        primaryStage.setScene(introScene());
        primaryStage.show();
    }


    private Graph initGraph(String filename){
        // Initialize gragh data structure
        Graph graph = new Graph("resources/" + filename);

        // Display graph
        graphDisplay = new GraphDisplay(graph);

//        MobileAgent test = new MobileAgent(graph.getStation(), graphDisplay,  true);
        // Start simulation
        graph.startThreads(graphDisplay);

        return graph;
    }

    private Scene introScene(){

        //root to this scene
        GridPane introRoot = new GridPane();

        Text title = new Text("MobileAgents");

        Button startButton = new Button("Start");
        startButton.setStyle(buttonStyle);
//        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED,handleConfirmation());

        ScrollPane graphselectionPane = graphSelectionScrollPane(startButton);

        Button infoBoxButton = new Button("what's this?");
        infoBoxButton.setStyle(buttonStyle);
        infoBoxButton.addEventHandler(MouseEvent.MOUSE_CLICKED,handleInfoBox());

        Text selectedFileText = new Text(fileSelction);

        Button getFileButton = new Button("get File");
        getFileButton.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileSelction = fileChooser.showOpenDialog(window).getName();

            selectedFileText.setText(fileSelction);
        });

        introRoot.setAlignment(Pos.CENTER);
//        introRoot.setPadding(new Insets(50, 10, 10, 10));
        introRoot.setHgap(25);

        introRoot.add(title, 1,1);
        introRoot.add(graphselectionPane, 1, 2);
        introRoot.add(getFileButton, 1, 3);
        introRoot.add(selectedFileText,1,4);

        introRoot.add(startButton, 2,2);
        introRoot.add(infoBoxButton, 3,2);

        return new Scene(introRoot, WIDTH, HEIGHT);
    }

    private EventHandler<MouseEvent> handleInfoBox() {

        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

            }
        };
    }

    private ScrollPane graphSelectionScrollPane(Button confirmButton) {

        ScrollPane scrollPane = new ScrollPane();

        ListView<String> listView = new ListView<String>();

        confirmButton.setOnAction(e->{

            String fileSelection = listView.getSelectionModel().getSelectedItem();

            if (fileSelection != null){
                initGraph(fileSelection);

            }

            window.setScene(new Scene(graphDisplay.getRoot(),WIDTH, HEIGHT));
        });

        ArrayList<String> filenamesList = new ArrayList<>();

        File folder = new File("resources");
        for (String file : folder.list()){
            filenamesList.add(file.substring(0,file.length() - 4));
        }

        ObservableList<String> items = FXCollections.observableArrayList (filenamesList);

        listView.setItems(items);

        scrollPane.setContent(listView);
//        scrollPane.setMinWidth(300);
        scrollPane.setMaxHeight(300);
        scrollPane.setFitToWidth(true);
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
