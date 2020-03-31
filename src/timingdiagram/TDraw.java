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
import javafx.geometry.Pos;
// event handling
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TDraw extends Application {
    // constants for styling
    private final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    private final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) {
        System.out.println("Java fx running");
        primaryStage.setTitle("TDraw");
        primaryStage.show();

        Button add_signal = new Button("Add Signal");
        add_signal.setStyle(IDLE_BUTTON_STYLE);

        add_signal.setOnMousePressed(e -> add_signal.setStyle(PRESSED_BUTTON_STYLE));
        add_signal.setOnMouseReleased(e -> add_signal.setStyle(IDLE_BUTTON_STYLE));

        add_signal.setOnAction((ActionEvent event) -> {
            System.out.println("pressed");
        });

        HBox buttons = new HBox(add_signal);

        HBox initial_signal_container = draw_new_signal_container();

        Group root = new Group();
        buttons.setPadding(new Insets(10));
        VBox signal_field = new VBox(initial_signal_container);

        VBox page = new VBox(buttons, signal_field);

        root.getChildren().add(page);

        Scene diagram = new Scene(root, 800, 400);
        primaryStage.setScene(diagram);
    }

    private HBox draw_new_signal_container() {
        Button delete_signal = new Button("X");
        delete_signal.setStyle(IDLE_BUTTON_STYLE);
        delete_signal.setOnMousePressed(e-> delete_signal.setStyle(PRESSED_BUTTON_STYLE));
        delete_signal.setOnMouseReleased(e-> delete_signal.setStyle(IDLE_BUTTON_STYLE));

        delete_signal.setOnAction((ActionEvent event) -> {
            System.out.println("pressed");
        });

        DSignal signal = new DSignal();
        HBox signal_container = new HBox(delete_signal, signal.draw());
        signal_container.setPadding(new Insets(10));
        signal_container.setSpacing(5);
        signal_container.setAlignment(Pos.BOTTOM_CENTER);

        return signal_container;
    }
}