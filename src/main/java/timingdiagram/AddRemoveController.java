package timingdiagram;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
// padding, spacing, etc
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// event handling
import javafx.event.ActionEvent;
import java.util.ArrayList;

public class AddRemoveController {
    // UI components: ---------------------------------------
    @FXML
    private VBox button_box;
    @FXML
    private VBox signal_box;
    // ------------------------------------------------------
    // Data fields: -----------------------------------------
    private final int MAX_SIGS = 10;
    private int num_sigs;
    private final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    private final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    private ArrayList<Button> buttons;
    private ArrayList<HBox> containers;

    private int index;
    // ------------------------------------------------------

    public void initialize() {
        num_sigs = 0;
        buttons = new ArrayList<>();
        containers = new ArrayList<>();
        index = 0;

        add_signal(); // add initial signal
    }

    protected VBox get_signal_box() {
        return signal_box;
    }

    protected void add_signal() {
        if (num_sigs > MAX_SIGS)
            return;

        num_sigs++;
        // add button
        Button delete_signal = new Button("X");
        delete_signal.setStyle(IDLE_BUTTON_STYLE);
        delete_signal.setOnMousePressed(e-> delete_signal.setStyle(PRESSED_BUTTON_STYLE));
        delete_signal.setOnMouseReleased(e-> delete_signal.setStyle(IDLE_BUTTON_STYLE));

        delete_signal.setOnAction((ActionEvent event) -> {
            remove_signal(delete_signal);
        });

        button_box.getChildren().add(delete_signal);
        button_box.setStyle("-fx-border-color: purple");

        // add signal
        DSignal signal = new DSignal();
        HBox signal_container = new HBox(signal.draw());
        signal_container.setPadding(new Insets(10));
        signal_container.setSpacing(5);
        signal_container.setAlignment(Pos.CENTER_LEFT);

        signal_box.getChildren().add(signal_container);

        // track index
        buttons.add(delete_signal);
        containers.add(signal_container);
        // set user data to be retrieved later
        signal_container.setUserData(index);
        delete_signal.setUserData(index);
        index++;
    }

    protected void remove_signal(Button b) {
        num_sigs--;
        int i = (int)b.getUserData();
        HBox container_to_remove = containers.get(i);
        Button button_to_remove = buttons.get(i);
        signal_box.getChildren().remove(container_to_remove);
        button_box.getChildren().remove(button_to_remove);
    }
}
