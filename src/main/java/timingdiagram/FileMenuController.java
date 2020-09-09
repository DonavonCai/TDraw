package timingdiagram;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class FileMenuController {
    @FXML
    protected MenuItem export_as_png;

    private FXMLController parent;

    public void setParent(FXMLController p) { parent = p; }

    public void initialize() {

    }

    public void initMenuItems() {
        init_export_as_png();
    }

    private void init_export_as_png() {
        export_as_png.setOnAction((ActionEvent event) -> {
            WritableImage image = parent.get_signal_box().snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save diagram to...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

            File file = fileChooser.showSaveDialog(parent.getPrimaryStage());

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                System.out.println("Failed to save.");
            }
        });
    }
}
