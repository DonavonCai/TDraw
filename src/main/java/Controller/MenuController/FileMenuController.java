package Controller.MenuController;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;

public class FileMenuController extends MenuController {
    // UI elements: ---------------------------------------------------
    @FXML
    private MenuItem new_project;
    @FXML
    private MenuItem open;
    @FXML
    private MenuItem save;
    @FXML
    private MenuItem export_as_png;

    // Inject these from parent controllers
    private VBox signalBox;
    private Stage primaryStage;

    // Setters and getters: ------------------------------------------
    public void SetSignalBox(VBox v) {
        signalBox = v;
    }

    public void SetPrimaryStage(Stage s) {
        primaryStage = s;
    }

    // Interface: ----------------------------------------------------
    public void initialize() {
//        init_new_project();
//        init_open();
//        init_save();
        init_export_as_png();
    }

//    private void init_new_project() {
//        new_project.setOnAction((ActionEvent event) -> {
//            parent.newProject();
//        });
//    }

//    private void init_open() {
//        open.setOnAction((ActionEvent event) -> {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Open...");
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TDraw", "*.tdraw"));
//
//            File file = fileChooser.showOpenDialog(parent.getPrimaryStage());
//
//            try {
//                FileInputStream fin = new FileInputStream(file.getPath());
//                ObjectInputStream obj = new ObjectInputStream(fin);
//
//                parent.open(obj);
//                fin.close();
//                obj.close();
//            } catch (Exception e) {
//                System.out.println("Failed to open file output stream:");
//                e.printStackTrace();
//            }
//        });
//    }

//    private void init_save() {
//        save.setOnAction((ActionEvent event) -> {
//            FileChooser fileChooser = new FileChooser();
//            fileChooser.setTitle("Save as...");
//            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TDraw", "*.tdraw"));
//
//            File file = fileChooser.showSaveDialog(parent.getPrimaryStage());
//
//            try {
//                FileOutputStream fout = new FileOutputStream(file.getPath());
//                ObjectOutputStream obj = new ObjectOutputStream(fout);
//                parent.save(obj);
//                fout.close();
//                obj.close();
//            } catch (Exception e) {
//                System.out.println("Failed to open file output stream:");
//                e.printStackTrace();
//            }
//        });
//    }

    private void init_export_as_png() {
        export_as_png.setOnAction((ActionEvent event) -> {
            WritableImage image = signalBox.snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save diagram to...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

            File file = fileChooser.showSaveDialog(primaryStage);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                System.out.println("Failed to save:");
                e.printStackTrace();
            }
        });
    }
}
