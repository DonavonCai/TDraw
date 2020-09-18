package timingdiagram.MenuController;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class FileMenuController extends MenuController {
    @FXML
    private MenuItem open;
    @FXML
    private MenuItem close;
    @FXML
    private MenuItem save;
    @FXML
    private MenuItem export_as_png;

    public void initialize() {
        init_open();
        init_save();
        init_export_as_png();
    }

    private void init_open() {
        open.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TDraw", "*.tdraw"));

            File file = fileChooser.showOpenDialog(parent.getPrimaryStage());

            try {
                FileInputStream fin = new FileInputStream(file.getPath());
                ObjectInputStream obj = new ObjectInputStream(fin);

                parent.open(file, obj);
                fin.close();
                obj.close();
            } catch (Exception e) {
                System.out.println("Failed to open file output stream:");
                e.printStackTrace();
            }
        });
    }

    private void init_save() {
        save.setOnAction((ActionEvent event) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save as...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TDraw", "*.tdraw"));

            File file = fileChooser.showSaveDialog(parent.getPrimaryStage());

            try {
                FileOutputStream fout = new FileOutputStream(file.getPath());
                ObjectOutputStream obj = new ObjectOutputStream(fout);
                parent.save(file, obj);
                fout.close();
                obj.close();
            } catch (Exception e) {
                System.out.println("Failed to open file output stream:");
                e.printStackTrace();
            }
        });
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
                System.out.println("Failed to save:");
                e.printStackTrace();
            }
        });
    }
}
