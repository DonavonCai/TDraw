package timingdiagram.SignalController;

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
import timingdiagram.DSignal.DSignal;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;

public class SignalController {
    // UI components: ---------------------------------------
    @FXML
    transient private HBox signalWrapper;
    @FXML
    transient private VBox button_box;
    @FXML
    transient private VBox signal_box;
    // ------------------------------------------------------
    // Serializable storage: -----------------------------------------
    private SignalControllerStorage storage;
    // ------------------------------------------------------

    public void initialize() {
        storage = new SignalControllerStorage();
        add_signal(); // add initial signal
    }

    public VBox get_signal_box() {
        return signal_box;
    }

    public void add_signal() {
        if (storage.num_sigs >= storage.MAX_SIGS)
            return;

        storage.num_sigs++;
        // add button
        Button delete_signal = createDeleteButton();
        delete_signal.setUserData(storage.num_sigs);
        button_box.getChildren().add(delete_signal);

        // add signal
        DSignal signal = new DSignal();
        HBox signal_container = createSignalContainer(signal);
        signal_box.getChildren().add(signal_container);

        signal_container.setUserData(storage.num_sigs);
        storage.signals.add(signal);
    }

    protected void remove_signal(Button b) {
        storage.num_sigs--;
        int i = (int)b.getUserData();
        HBox container_to_remove = (HBox)getByUserData(signal_box, i);
        signal_box.getChildren().remove(container_to_remove);
        button_box.getChildren().remove(b);
    }

    private Button createDeleteButton() {
        Button b = new Button("X");
        b.setStyle(storage.IDLE_BUTTON_STYLE);
        b.setOnMousePressed(e-> b.setStyle(storage.PRESSED_BUTTON_STYLE));
        b.setOnMouseReleased(e-> b.setStyle(storage.IDLE_BUTTON_STYLE));
        b.setOnAction((ActionEvent event) -> {
            remove_signal(b);
        });
        return b;
    }

    private HBox createSignalContainer(DSignal d) {
        HBox signal_container = new HBox(d.getDiagram());
        signal_container.setPadding(new Insets(10));
        signal_container.setAlignment(Pos.CENTER_LEFT);
        return signal_container;
    }

    public void newProject() {
        button_box.getChildren().clear();
        signal_box.getChildren().clear();
        storage.num_sigs = 0;
        add_signal();
    }

    public void save(ObjectOutputStream obj) {
        try {
            obj.writeObject(storage);
        } catch (Exception e) {
            System.out.println("Failed to save:");
            e.printStackTrace();
        }
    }

    public void open(ObjectInputStream obj) {
        try {
            button_box.getChildren().clear();
            signal_box.getChildren().clear();
            storage = (SignalControllerStorage)obj.readObject();
            reconstruct();
        } catch (Exception e) {
            e.printStackTrace();
            exit(1);
        }
    }

    public void reconstruct() {
        for (int i = 0; i < storage.signals.size(); i++) {
            DSignal signal = storage.signals.get(i);
            signal.redraw();

            // create button and container for newly redrawn signal
            Button b = createDeleteButton();
            b.setUserData(i + 1);

            HBox container = createSignalContainer(signal);
            container.setUserData(i + 1);

            button_box.getChildren().add(b);
            signal_box.getChildren().add(container);
        }
    }

    public HBox getWrapper() { return signalWrapper; }

    private Node getByUserData(VBox parent, Object data) {
        for (Node n : parent.getChildren()) {
            if (data.equals(n.getUserData())) {
                return n;
            }
        }
        return null;
    }
}
