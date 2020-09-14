package timingdiagram;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
// padding, spacing, etc
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// event handling
import javafx.event.ActionEvent;
import timingdiagram.Signal.DSignal;
// saving
import java.io.Serializable;

import java.util.ArrayList;

public class SignalController implements Serializable {
    // UI components: ---------------------------------------
    @FXML
    transient private HBox signalWrapper;
    @FXML
    transient private VBox button_box;
    @FXML
    transient private VBox signal_box;
    // ------------------------------------------------------
    // Data fields: -----------------------------------------
    private final int MAX_SIGS = 15;
    private int num_sigs;
    private final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    private final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    private ArrayList<DSignal> signals;

    // ------------------------------------------------------

    public void initialize() {
        num_sigs = 0;
        signals = new ArrayList<>();
        add_signal(); // add initial signal
    }

    public VBox get_signal_box() {
        return signal_box;
    }

    public void restyle() {
        signalWrapper.setStyle("-fx-padding: 10");
        button_box.setSpacing(33);
        button_box.setPadding(new Insets(10));
        button_box.setAlignment(Pos.CENTER_LEFT);
        signal_box.setSpacing(10);
        signal_box.setAlignment(Pos.BOTTOM_CENTER);
    }

    protected void add_signal() {
        if (num_sigs >= MAX_SIGS)
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
        signal_container.setAlignment(Pos.CENTER_LEFT);

        signal_box.getChildren().add(signal_container);

        // set user data to be retrieved later
        signal_container.setUserData(num_sigs);
        delete_signal.setUserData(num_sigs);

        signals.add(signal);
    }

    protected void remove_signal(Button b) {
        num_sigs--;
        int i = (int)b.getUserData();
        HBox container_to_remove = (HBox)getByUserData(signal_box, i);
        signal_box.getChildren().remove(container_to_remove);
        button_box.getChildren().remove(b);
    }

    protected void reconstruct() {
        button_box = new VBox();
        signal_box = new VBox();
        signalWrapper = new HBox(button_box, signal_box);
        restyle();
        for (int i = 0; i < signals.size(); i++) {
            signals.get(i).reconstruct(); // todo: implement this
        }
    }

    public HBox getWrapper() {
        return signalWrapper;
    }

    private Node getByUserData(VBox parent, Object data) {
        for (Node n : parent.getChildren()) {
            if (data.equals(n.getUserData())) {
                return n;
            }
        }
        return null;
    }
}
