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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class Main extends Application {

    private String fileSelectionStr = "sample";
    private File fileSelection;
    public static final int WIDTH = 700;
    public static final int HEIGHT = 500;
    private GraphDisplay graphDisplay;
    private Stage window;
    private String buttonStyle =  "    -fx-text-fill: #101024;\n" +
            "    -fx-background-color: #EEEEEE;\n" +
            "    -fx-border-color: #111111;" +
            "    -fx-padding: 8;";
    private Text selectedFileText;

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        //ends all threads and closes window
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
            primaryStage.close();
        });

        primaryStage.setTitle("Mobile Agents");
        primaryStage.setScene(introScene());
        primaryStage.show();
    }


    private Graph initGraph(File file){
        // Initialize gragh data structure
        Graph graph = new Graph(file);

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

        //set Title
        Text title = new Text("Mobile Agents");
        title.setFont(Font.font(70));
        title.setStyle("-fx-font: 24 arial;");

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
        selectedFileText = new Text(fileSelectionStr);

        //button that opens a file selector
        Button openButton = new Button("Open");
        openButton.setStyle(buttonStyle);
        openButton.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
//            fileChooser.setInitialDirectory(new File("resources"));

            try {
//                fileSelectionStr = fileChooser.showOpenDialog(window).getName();
                fileSelection = fileChooser.showOpenDialog(window);

            }catch (NullPointerException e){return;}

            selectedFileText.setText(fileSelectionStr.substring(0, fileSelectionStr.length() - 4));

        });

        //add everything to the scene root
        introRoot.setAlignment(Pos.CENTER);
        introRoot.setHgap(25);
        introRoot.setVgap(15);

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
        File folder = new File("resources");
        ObservableList<String> items = FXCollections.observableArrayList (Arrays.asList(folder.list()));

        listView.setItems(items);

        //Prepare scroll pane
        scrollPane.setContent(listView);
        scrollPane.setMaxHeight(300);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    /**
     * Handles startingt the simulation making sure a file is selected and changing the scene.
     * @return
     */
    private EventHandler<MouseEvent> handleConfirm() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                //if a file is selected, load up Graph and start the simulation
                if (fileSelection != null){
                    initGraph(fileSelection);
                    window.setScene(new Scene(graphDisplay.getRoot(),WIDTH, HEIGHT));
                } else {
                    selectedFileText.setText("Please Choose File");
                }
            }
        };
    }

    private EventHandler<MouseEvent> handleListClick(ListView<String> listView) {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                fileSelectionStr = listView.getSelectionModel().getSelectedItem();

                fileSelection = new File("resources/" + fileSelectionStr);
                selectedFileText.setText(fileSelectionStr.substring(0, fileSelectionStr.length() - 4));

            }
        };
    }


    public static void main(String[] args) {

        launch(args);
    }
}
