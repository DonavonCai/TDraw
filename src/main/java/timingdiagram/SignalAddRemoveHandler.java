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
    private final int MAX_SIGS = 10;
    private int num_sigs;
    private final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    private final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    private final ArrayList<Button> buttons;
    private final ArrayList<HBox> containers;

    private final VBox signal_box;
    private final VBox button_box;

    private int index;

    // note: userData for buttons and containers is based on num_signals
    SignalAddRemoveHandler(HBox sig) {
        num_sigs = 1; // because we start with 1 initial signal
        signal_box = new VBox();

        // The signal box is what we export as pdf. Set the userdata so that the signal box can be accessed with getUserData("signalBox").
        String SIGNAL_BOX_USERDATA = "signalBox";
        signal_box.setUserData(SIGNAL_BOX_USERDATA);
        button_box = new VBox();
        sig.getChildren().add(button_box);
        sig.getChildren().add(signal_box);

        button_box.setSpacing(24);
        button_box.setAlignment(Pos.CENTER_LEFT);
        signal_box.setAlignment(Pos.BOTTOM_CENTER);
        sig.setPadding(new Insets(10));

        buttons = new ArrayList<>();
        containers = new ArrayList<>();
        index = 0;
    }

    protected VBox get_signal_box() {return signal_box;}

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

        // add signal
        DSignal signal = new DSignal();
        HBox signal_container = new HBox(signal.draw());
        signal_container.setPadding(new Insets(10));
        signal_container.setSpacing(5);
        signal_container.setAlignment(Pos.BOTTOM_CENTER);

        signal_box.getChildren().add(signal_container);

        // track index
        buttons.add(delete_signal);
        containers.add(signal_container);
        // set user data to be retrieved later
        signal_container.setUserData(index);
        delete_signal.setUserData(index);

        signal_container.setAlignment(Pos.CENTER_LEFT);
        signal_container.setPadding(new Insets(10));
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
