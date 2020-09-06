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

import static java.lang.System.exit;

public class FXMLController {
    // UI components: ---------------------------------------
    @FXML
    private Group root;
    @FXML
    private VBox page;
    @FXML
    private MenuItem export_as_png;
    @FXML
    private VBox buttons;

    private Stage primaryStage;
    // ------------------------------------------------------
    // Helper classes: --------------------------------------
    @FXML
    private AddRemoveController addRemoveController;
    // ======================================================

    // Interface: -------------------------------------------
    public void initialize() {
        System.out.println("FXML controller initialized");
        initMenuEvents();
    }

    public void setStageAndBindWidth(Stage s) { // bind width of 'page' to width of primary stage
        primaryStage = s;
        page.prefWidthProperty().bind(s.widthProperty());
    }
    // ------------------------------------------------------
    // Helper Functions: ------------------------------------
    private void initMenuEvents() {
        export_as_png.setOnAction((ActionEvent event) -> {
            WritableImage image = addRemoveController.get_signal_box().snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save diagram to...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

            File file = fileChooser.showSaveDialog(primaryStage);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                System.out.println("Failed to save.");
            }
        });
    }
    // ------------------------------------------------------
    // Functions called in .fxml files ----------------------
    @FXML
    private void addSignal() {
        System.out.println("add signal");
        addRemoveController.add_signal();
    }
}
