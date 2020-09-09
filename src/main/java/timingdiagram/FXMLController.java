package timingdiagram;

// Layout
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.fxml.FXML;

// Event handling
import javafx.event.ActionEvent;
import java.io.File;
import javafx.stage.FileChooser;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.IOException;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;

public class FXMLController {
    // UI components: ---------------------------------------
    @FXML
    private Group root;
    @FXML
    private VBox page;
    @FXML
    private MenuItem export_as_png;

    private Stage primaryStage;
    // ------------------------------------------------------
    // Helper classes: --------------------------------------
    @FXML
    private AddRemoveController addRemoveController;
    @FXML
    private FileMenuController fileMenuController;
    // ======================================================

    // Interface: -------------------------------------------
    public void initialize() {
        System.out.println("FXML controller initialized");
        if (fileMenuController == null) { // fixme: why is this null?
            System.out.println("NULL!");
        }
        initMenuEvents();
    }

    public void setStageAndBindWidth(Stage s) { // bind width of 'page' to width of primary stage
        primaryStage = s;
        page.prefWidthProperty().bind(s.widthProperty());
    }

    protected VBox get_signal_box() { return addRemoveController.get_signal_box(); }

    protected Stage getPrimaryStage() { return primaryStage;}
    // ------------------------------------------------------
    // Helper Functions: ------------------------------------
    private void initMenuEvents() {
        fileMenuController.setParent(this);
        fileMenuController.initMenuItems();
    }
    // ------------------------------------------------------
    // Functions called in .fxml files ----------------------
    @FXML
    private void addSignal() {
        System.out.println("add signal");
        addRemoveController.add_signal();
    }
}
