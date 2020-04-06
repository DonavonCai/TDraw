package timingdiagram;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
// padding, spacing, etc
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// event handling
import javafx.event.ActionEvent;
import java.util.ArrayList;

public class SignalAddRemoveHandler {
    private final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    private final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    // note: userData for buttons and containers is based on num_signals
    private VBox signal_field;
    private ArrayList<Button> buttons;
    private ArrayList<HBox> containers;

    private int index;

    SignalAddRemoveHandler() {
        System.out.println("signal handler created");
        buttons = new ArrayList<Button>();
        containers = new ArrayList<HBox>();
        index = 0;
    }

    void add_signal(VBox signal_field) {
        this.signal_field = signal_field;
        HBox new_signal_container = draw_new_signal_container();
        signal_field.getChildren().add(new_signal_container);
    }

    void remove_signal(Button b) {
        Integer i = (int)b.getUserData();
        System.out.println("i: " + i);
        HBox container_to_remove = containers.get(i);
        this.signal_field.getChildren().remove(container_to_remove);
    }

    private HBox draw_new_signal_container() {
        Button delete_signal = new Button("X");
        delete_signal.setStyle(IDLE_BUTTON_STYLE);
        delete_signal.setOnMousePressed(e-> delete_signal.setStyle(PRESSED_BUTTON_STYLE));
        delete_signal.setOnMouseReleased(e-> delete_signal.setStyle(IDLE_BUTTON_STYLE));

        delete_signal.setOnAction((ActionEvent event) -> {
            System.out.println("pressed");
            remove_signal(delete_signal);
        });

        DSignal signal = new DSignal();
        HBox signal_container = new HBox(delete_signal, signal.draw());
        signal_container.setPadding(new Insets(10));
        signal_container.setSpacing(5);
        signal_container.setAlignment(Pos.BOTTOM_CENTER);

        buttons.add(delete_signal);
        containers.add(signal_container);
        // set user data to be retrieved later
        signal_container.setUserData(index);
        delete_signal.setUserData(index);

        signal_container.setAlignment(Pos.CENTER_LEFT);
        signal_container.setPadding(new Insets(10));
        index++;

        return signal_container;
    }
}
