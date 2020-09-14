package timingdiagram;

// Layout
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.fxml.FXML;

// saving
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// Event handling
import timingdiagram.MenuController.FileMenuController;

import static java.lang.System.exit;

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

    public void save(File file, ObjectOutputStream obj) {
        try {
            obj.writeObject(signalController);
        } catch (Exception e) {
            System.out.println("Failed to save:");
            e.printStackTrace();
        }
    }

    public void open(File file, ObjectInputStream obj) {
        try {
            page.getChildren().remove(signalController.getWrapper());
            signalController = (SignalController)obj.readObject();
            signalController.reconstruct();
            page.getChildren().add(signalController.getWrapper());
        } catch (Exception e) {
            e.printStackTrace();
//            exit(1);
        }
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
