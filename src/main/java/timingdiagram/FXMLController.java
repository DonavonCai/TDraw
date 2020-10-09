package timingdiagram;

// Layout
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;

// saving
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Event handling
import timingdiagram.MenuController.FileMenuController;
import timingdiagram.SignalController.SignalController;

public class FXMLController {
    // UI components: ---------------------------------------
    @FXML
    private VBox page;

    private Stage primaryStage;
    // ------------------------------------------------------
    // Helper classes: --------------------------------------
    @FXML
    private SignalController signalController;
    @FXML
    private FileMenuController fileMenuController;
    // ======================================================

    // Interface: -------------------------------------------
    public void initialize() {
        initMenus();
    }

    public void setStageAndBindWidth(Stage s) { // bind width of 'page' to width of primary stage
        primaryStage = s;
        page.prefWidthProperty().bind(s.widthProperty());
    }

    public VBox get_signal_box() { return signalController.get_signal_box(); }

    public Stage getPrimaryStage() { return primaryStage;}

    public void newProject() {
        signalController.newProject();
    }

    public void save(ObjectOutputStream obj) {
        signalController.save(obj);
    }

    public void open(ObjectInputStream obj) {
        signalController.open(obj);
    }

    // ------------------------------------------------------
    // Helper Functions: ------------------------------------
    private void initMenus() {
        fileMenuController.setParent(this);
    }
    // ------------------------------------------------------
    // Functions called in .fxml files ----------------------
    @FXML
    private void addSignal() {
        signalController.add_signal();
    }
}
