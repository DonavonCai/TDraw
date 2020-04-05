package timingdiagram;
// grouping
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
// padding, spacing, etc
import javafx.geometry.Insets;
// event handling
import javafx.event.ActionEvent;
// pdf
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
import java.io.File;
import javafx.stage.FileChooser;
import java.io.IOException;

import javafx.scene.image.Image;

public class TDraw extends Application {
    // constants for styling
    private final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    private final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    // handlers
    private SignalAddRemoveHandler signal_handler;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) {
        System.out.println("Java fx running");
        primaryStage.setTitle("TDraw");
        primaryStage.getIcons().add(new Image("img/logo.jpg"));
        primaryStage.show();

        signal_handler = new SignalAddRemoveHandler();

        Group root = new Group();

        // initialize first signal + signal field
        VBox signal_field = new VBox();
        signal_handler.add_signal(signal_field);

        // initialize buttons
        Button export_button = new Button("Export as PDF");
        initialize_style(export_button);

        export_button.setOnAction((ActionEvent event) -> {
            System.out.println("saving!");
            WritableImage image = signal_field.snapshot(new SnapshotParameters(), null);
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save diagram to...");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG", "*.png"));

            File file = fileChooser.showSaveDialog(primaryStage);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                System.out.println("Failed to save.");
                return;
            }
        });

        Button add_signal = new Button("Add Signal");
        initialize_style(add_signal);

        add_signal.setOnAction((ActionEvent event) -> {
            signal_handler.add_signal(signal_field);
        });

        VBox buttons = new VBox(export_button, add_signal);
        buttons.setPadding(new Insets(10));
        buttons.setSpacing(10.0);

        VBox page = new VBox(buttons, signal_field);
        root.getChildren().add(page);

        Scene diagram = new Scene(root, 900, 500);
        primaryStage.setScene(diagram);
    }

    void initialize_style(Button b) {
        b.setStyle(IDLE_BUTTON_STYLE);
        b.setOnMousePressed(e -> b.setStyle(PRESSED_BUTTON_STYLE));
        b.setOnMouseReleased(e -> b.setStyle(IDLE_BUTTON_STYLE));
    }
}