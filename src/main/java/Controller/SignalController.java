package Controller;

import Model.DiagramModel;
import TimingDiagram.DSignal.DSignal;
import View.SignalView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SignalController {
    // Constants: -------------------------------------------
    protected final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    protected final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    // Members: ------------------------------------------------------
    DiagramModel model;

    // UI components: ---------------------------------------
    @FXML
    private HBox signalWrapper;
    @FXML
    private VBox buttonBox;
    @FXML
    private VBox signalBox;

    // Functions used in FXML: ---------------------------------------
    @FXML
    public void initialize() {
        model = new DiagramModel();
        signalWrapper.setFillHeight(false); // wrapper will not resize children
        AddSignal();
    }

    @FXML
    public void AddSignal() {
        // Notify model, and give it a view
        SignalView signalView = new SignalView();
        model.AddSignal(signalView);

        // add button
        Button deleteSignal = createDeleteButton();
        deleteSignal.setUserData(model.GetNumSigs());
        buttonBox.getChildren().add(deleteSignal);

        HBox signalContainer = new HBox((signalView.GetDiagram()));
        signalContainer.setAlignment(Pos.CENTER_LEFT);
        signalBox.getChildren().add(signalContainer);

        signalContainer.setUserData(model.GetNumSigs());
    }

    // Helpers: ---------------------------------------------------------
    private Button createDeleteButton() {
        Button b = new Button("X");
        b.setStyle(IDLE_BUTTON_STYLE);
        b.setOnMousePressed(e-> b.setStyle(PRESSED_BUTTON_STYLE));
        b.setOnMouseReleased(e-> b.setStyle(IDLE_BUTTON_STYLE));
        b.setOnAction((ActionEvent event) -> {
            remove_signal(b);
        });
        return b;
    }

    protected void remove_signal(Button b) {
        // Remove signal from model
        int i = (int)b.getUserData();
        model.RemoveSignal(i);

        // Remove signal from view
        HBox container_to_remove = (HBox)getByUserData(signalBox, i);
        signalBox.getChildren().remove(container_to_remove);
        buttonBox.getChildren().remove(b);
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
