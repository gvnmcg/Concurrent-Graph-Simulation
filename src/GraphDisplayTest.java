import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class GraphDisplayTest extends Application {

    public static boolean debugMessaging = false;
    public static boolean debugMobileAgents = false;
    public static boolean debugGraphNode = false;

    String fileSelection = "sample";

    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;

    GraphDisplay graphDisplay;

    Stage window;

    String buttonStyle =  "    -fx-text-fill: #006464;\n" +
            "    -fx-background-color: #DFB951;\n" +
            "    -fx-border-radius: 20;\n" +
            "    -fx-background-radius: 20;\n" +
            "    -fx-padding: 5;";
    Text selectedFileText;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        //ends all threads and closes window
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
            primaryStage.close();
        });

        primaryStage.setScene(introScene());
        primaryStage.show();
    }


    private Graph initGraph(String filename){
        // Initialize gragh data structure
        Graph graph = new Graph("resources/" + filename);

        // Display graph
        graphDisplay = new GraphDisplay(graph);

        new MobileAgent(graph.getStation(), graphDisplay,  true);
        // Start simulation
        graph.startThreads();

        return graph;
    }

    private Scene introScene(){

        //root to this scene
        GridPane introRoot = new GridPane();

        Text title = new Text("MobileAgents");

        //Button that confirms the selection and starts the simulation
        Button startButton = new Button("Start");
        startButton.setStyle(buttonStyle);
        startButton.addEventHandler(MouseEvent.MOUSE_CLICKED, handleConfirm());

        //pane containing all the graph options
        ScrollPane graphselectionPane = graphSelectionScrollPane();

        //opens a dialog giving infor mationa about the project
        Button infoBoxButton = new Button("Info");
        infoBoxButton.setStyle(buttonStyle);
        infoBoxButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);

            alert.setGraphic(null);
            alert.setTitle("");
            alert.setHeaderText("What is Mobile Agents?");
            alert.setContentText("It is a simulation of a communication network as it launches concurrent programs " +
                    "to contain a 'forest fire' destroying the nodes on the network.");

//            alert.setOnCloseRequest(event -> alert.close());
            alert.showAndWait();
            alert.close();
        });

        //tells what file is selected byt he selector
        selectedFileText = new Text(fileSelection);

        //button that opens a file selector
        Button openButton = new Button("Open");
        openButton.setStyle(buttonStyle);
        openButton.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("resources"));

            try {
                fileSelection = fileChooser.showOpenDialog(window).getName();

            }catch (NullPointerException e){return;}

            selectedFileText.setText(fileSelection.substring(0, fileSelection.length() - 4));

        });

        //add everything to the scene root

        introRoot.setAlignment(Pos.CENTER);
        introRoot.setHgap(25);

        introRoot.add(title, 1,1);
        introRoot.add(graphselectionPane, 1, 2);
        introRoot.add(openButton, 1, 3);
        introRoot.add(selectedFileText,1,4);

        introRoot.add(startButton, 2,2);
        introRoot.add(infoBoxButton, 3,2);

        return new Scene(introRoot, WIDTH, HEIGHT);
    }

    private ScrollPane graphSelectionScrollPane() {

        ScrollPane scrollPane = new ScrollPane();

        //selectable list of options
        ListView<String> listView = new ListView<String>();

        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, handleListClick(listView));

        //read in file names into list for listview
        ArrayList<String> filenamesList = new ArrayList<>();
        File folder = new File("resources");
        for (String file : folder.list()){
            filenamesList.add(file);
        }

        ObservableList<String> items = FXCollections.observableArrayList (filenamesList);

        listView.setItems(items);

        //prepare scroll pane
        scrollPane.setContent(listView);
        scrollPane.setMaxHeight(300);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    private EventHandler<MouseEvent> handleConfirm() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (fileSelection != null){
                    initGraph(fileSelection);
                    window.setScene(new Scene(graphDisplay.getRoot(),WIDTH, HEIGHT));
                }
            }
        };
    }

    private EventHandler<MouseEvent> handleListClick(ListView<String> listView) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fileSelection = listView.getSelectionModel().getSelectedItem();
                selectedFileText.setText(fileSelection.substring(0, fileSelection.length() - 4));

            }
        };
    }


    public static void main(String[] args) {

        launch(args);
    }
}
