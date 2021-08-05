package TimingDiagram.SignalController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
// padding, spacing, etc
import javafx.geometry.Pos;
// event handling
import javafx.event.ActionEvent;
import TimingDiagram.DSignal.DSignal;
import TimingDiagram.SignalController.WidthController.WidthSelectorController;
// saving + opening
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;

public class SignalController {
    // Constants: -------------------------------------------
    public final int MIN_CANVAS_WIDTH = 200;
    public final int MAX_CANVAS_WIDTH = 1000;
    public final int LINE_WIDTH = 3;
    public final int NAME_WIDTH = 100;
    public final int SIGNAL_HEIGHT = 30;

    // UI components: ---------------------------------------
    @FXML
    private HBox signalWrapper;
    @FXML
    private VBox button_box;
    @FXML
    private VBox signal_box;

    // Child Controllers: -----------------------------------
    @FXML
    private WidthSelectorController widthSelectorController;

    // Data : -----------------------------------------------
    private SignalControllerStorage storage;

    // Setters and getters: ---------------------------------
    public VBox get_signal_box() {
        return signal_box;
    }

    // Initialization: --------------------------------------
    public void initialize() {
        signalWrapper.setFillHeight(false); // wrapper will not resize children
        storage = new SignalControllerStorage();
        add_signal(); // add initial signal
        widthSelectorController.setParentAndBindHeight(this);
    }

    // Interface: -------------------------------------------
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

    public double getCanvasWidth() {
        return storage.getCanvasWidth();
    }

    public void setSignalWidths(double w) {
        System.out.println("canvas width is now: " + storage.getCanvasWidth());
        System.out.println("changing to: " + w);
        storage.setCanvasWidth(w);
        for (int i = 0; i < storage.signals.size(); i++) {
            storage.signals.get(i).setCanvasWidth(w);
        }
    }

    // Button functions: ----------------------------------
    public void add_signal() {
        if (storage.num_sigs >= storage.MAX_SIGS)
            return;

        storage.num_sigs++;
        // add button
        Button delete_signal = createDeleteButton();
        delete_signal.setUserData(storage.num_sigs);
        button_box.getChildren().add(delete_signal);

        // add signal
        DSignal signal = new DSignal(this);
        HBox signal_container = createSignalContainer(signal);
        signal_box.getChildren().add(signal_container);

        signal_container.setUserData(storage.num_sigs);
        storage.signals.add(signal);
        // todo: set width function?
    }

    protected void remove_signal(Button b) {
        storage.num_sigs--;
        int i = (int)b.getUserData();
        HBox container_to_remove = (HBox)getByUserData(signal_box, i);
        signal_box.getChildren().remove(container_to_remove);
        button_box.getChildren().remove(b);
    }

    // Helper Functions: ---------------------------------
    private Node getByUserData(VBox parent, Object data) {
        for (Node n : parent.getChildren()) {
            if (data.equals(n.getUserData())) {
                return n;
            }
        }
        return null;
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
        signal_container.setAlignment(Pos.CENTER_LEFT);
        return signal_container;
    }

    private void reconstruct() {
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
}
