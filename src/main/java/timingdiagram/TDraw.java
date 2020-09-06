package timingdiagram;
// grouping
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
// padding, spacing, layout
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// event handling
import javafx.event.ActionEvent;
// pdf saving
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import javafx.stage.FileChooser;
import java.io.IOException;
// icon
import javafx.scene.image.Image;
// fxml
import javafx.fxml.FXMLLoader;
import static java.lang.System.exit;

public class TDraw extends Application {
    // constants for styling
    private final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    private final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) {
        // Title and Icon:
        primaryStage.setTitle("TDraw");
        try {
            primaryStage.getIcons().add(new Image("/logo.jpg"));
        } catch (Exception err) {
            System.out.println("Failed to load program logo:\n");
            err.printStackTrace();
        }
        primaryStage.show();

        // Layout:
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/TDraw.fxml"));
        Group root;
        try {
            root = loader.<Group>load();
            Scene diagram = new Scene(root, 900, 500);
            primaryStage.setScene(diagram);
        } catch (Exception err) {
            System.out.println("Failed to load root:\n");
            err.printStackTrace();
            exit(1);
        }

        FXMLController controller = loader.getController();
        controller.setStageAndBindWidth(primaryStage); // updates width of page to stage passed in
/*
        // initialize first signal + signal field
        HBox signalWrapper = new HBox();
        signal_handler = new SignalAddRemoveHandler(signalWrapper);
        signal_handler.add_signal();

        // initialize menu
        Menu m = new Menu("File");
        MenuItem export_as_pdf = new MenuItem("Export as PDF");
        m.getItems().add(export_as_pdf);
        MenuBar mb = new MenuBar();
        mb.prefWidthProperty().bind(primaryStage.widthProperty());
        mb.getMenus().add(m);

        export_as_pdf.setOnAction((ActionEvent event) -> {
            System.out.println("saving!");
            VBox signal_field_only = signal_handler.get_signal_box();
            WritableImage image = signal_field_only.snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save diagram to...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

            File file = fileChooser.showSaveDialog(primaryStage);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                System.out.println("Failed to save.");
                exit(0);
            }
        });

        // initialize buttons
        Button add_signal = new Button("Add Signal");
        initialize_style(add_signal);

        add_signal.setOnAction((ActionEvent event) -> {
            signal_handler.add_signal();
        });

        // layout
        VBox buttons = new VBox(add_signal);
        buttons.setPadding(new Insets(10));
        buttons.setSpacing(10.0);

        VBox page = new VBox(mb, buttons, signalWrapper);
        page.setAlignment(Pos.BASELINE_LEFT);
        page.setSpacing(10);
        root.getChildren().add(page);

        Scene diagram = new Scene(root, 900, 500);
        primaryStage.setScene(diagram);
*/

    }

    void initialize_style(Button b) {
        b.setStyle(IDLE_BUTTON_STYLE);
        b.setOnMousePressed(e -> b.setStyle(PRESSED_BUTTON_STYLE));
        b.setOnMouseReleased(e -> b.setStyle(IDLE_BUTTON_STYLE));
    }
}