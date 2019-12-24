package timingdiagram;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class TDraw extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    public void start(Stage primaryStage) {
        System.out.println("Java fx running");
        primaryStage.setTitle("TDraw");
        primaryStage.show();

        Button add_signal_button = new Button("Add Signal");

        // TODO: find a layout for this
        DSignal initial_signal = new DSignal();
        Group root = new Group();
        HBox buttons = new HBox(add_signal_button);
        VBox labels = new VBox(initial_signal.draw());

        root.getChildren().add(buttons);
        root.getChildren().add(labels);

        Scene diagram = new Scene(root, 800, 400);
        primaryStage.setScene(diagram);
    }
}