import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
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

        MobileAgent test = new MobileAgent(graph.getStation(), graphDisplay,  true);
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

        //pane containing all the graph options
        ScrollPane graphselectionPane = graphSelectionScrollPane(startButton);

        //opens a dialog giving infor mationa about the project
        Button infoBoxButton = new Button("what's this?");
        infoBoxButton.setStyle(buttonStyle);
        infoBoxButton.setOnAction(event -> {

        });

        //tells what file is selected byt he selector
        selectedFileText = new Text(fileSelection);

        //button that opens a file selector
        Button getFileButton = new Button("get File");
        getFileButton.setStyle(buttonStyle);
        getFileButton.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileSelection = fileChooser.showOpenDialog(window).getName();

            selectedFileText.setText(fileSelection);
        });

        //add everything to the scene root

        introRoot.setAlignment(Pos.CENTER);
        introRoot.setHgap(25);

        introRoot.add(title, 1,1);
        introRoot.add(graphselectionPane, 1, 2);
        introRoot.add(getFileButton, 1, 3);
        introRoot.add(selectedFileText,1,4);

        introRoot.add(startButton, 2,2);
        introRoot.add(infoBoxButton, 3,2);

        return new Scene(introRoot, WIDTH, HEIGHT);
    }

    private ScrollPane graphSelectionScrollPane(Button confirmButton) {

        ScrollPane scrollPane = new ScrollPane();

        //selectable list of options
        ListView<String> listView = new ListView<String>();

        listView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectedFileText.setText(listView.getSelectionModel().getSelectedItem());
            }
        });

        //button to start simulation takes what ever is selected
        confirmButton.setOnAction(e->{

            fileSelection = listView.getSelectionModel().getSelectedItem();
            if (fileSelection != null){
                initGraph(fileSelection);
                window.setScene(new Scene(graphDisplay.getRoot(),WIDTH, HEIGHT));
            }
        });

        //read in file names into list for listview
        ArrayList<String> filenamesList = new ArrayList<>();
        File folder = new File("resources");
        for (String file : folder.list()){
            filenamesList.add(file.substring(0,file.length() - 4));
        }

        ObservableList<String> items = FXCollections.observableArrayList (filenamesList);

        listView.setItems(items);

        //prepare scroll pane
        scrollPane.setContent(listView);
        scrollPane.setMaxHeight(300);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }


    public static void main(String[] args) {

        launch(args);
    }
}
