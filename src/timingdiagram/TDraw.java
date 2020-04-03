package timingdiagram;
// grouping
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
// padding, spacing, etc
import javafx.geometry.Insets;
// event handling
import javafx.event.ActionEvent;

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
        primaryStage.show();

        signal_handler = new SignalAddRemoveHandler();

        Group root = new Group();

        // initialize first signal + signal field
        VBox signal_field = new VBox();
        signal_handler.add_signal(signal_field);

        // initialize add signal button
        Button add_signal = new Button("Add Signal");
        add_signal.setStyle(IDLE_BUTTON_STYLE);

        add_signal.setOnMousePressed(e -> add_signal.setStyle(PRESSED_BUTTON_STYLE));
        add_signal.setOnMouseReleased(e -> add_signal.setStyle(IDLE_BUTTON_STYLE));

        add_signal.setOnAction((ActionEvent event) -> { // functionality for adding new signals
            signal_handler.add_signal(signal_field);
        });

        HBox buttons = new HBox(add_signal);
        buttons.setPadding(new Insets(10));

        VBox page = new VBox(buttons, signal_field);
        root.getChildren().add(page);

        Scene diagram = new Scene(root, 900, 500);
        primaryStage.setScene(diagram);
    }
}