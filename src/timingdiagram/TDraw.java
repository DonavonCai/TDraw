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
import javafx.event.EventHandler;

public class TDraw extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) {
        System.out.println("Java fx running");
        primaryStage.setTitle("TDraw");
        primaryStage.show();

        Button add_signal_button = new Button("Add Signal");
        EventHandler<ActionEvent> add_signal = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                // TODO: add new signal
            }
        };
        add_signal_button.setOnAction(add_signal);

        DSignal initial_signal = new DSignal();
        Group root = new Group();
        HBox buttons = new HBox(add_signal_button);
        buttons.setPadding(new Insets(10));
        VBox diagrams = new VBox(initial_signal.draw());

        VBox document = new VBox(buttons, diagrams);

        root.getChildren().add(document);

        Scene diagram = new Scene(root, 800, 400);
        primaryStage.setScene(diagram);
    }
}