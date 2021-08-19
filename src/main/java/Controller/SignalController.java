package Controller;

import Controller.Helper.MouseEventHandler;
import Controller.MenuController.FileMenuController;
import Model.DiagramModel;
import Model.Signal;
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
    MouseEventHandler eventHandlerCreator;
    private int leftBound;
    private int rightBound;

    // UI components: -----------------------------------------------
    @FXML
    private HBox signalWrapper;
    @FXML
    private VBox buttonBox;
    @FXML
    private VBox signalBox;

    // Setters and getters: ------------------------------------------
    public VBox GetSignalBox() {
        return signalBox;
    }

    // Functions used in FXML: ---------------------------------------
    @FXML
    public void initialize() {
        leftBound = 0;
        rightBound = 700;
        model = new DiagramModel();
        eventHandlerCreator = new MouseEventHandler();
        signalWrapper.setFillHeight(false); // wrapper will not resize children
        AddSignal();
    }

    @FXML
    public void AddSignal() {
        // Create a model and give it a view
        Signal signalModel = new Signal();
        SignalView signalView = new SignalView();
        signalModel.SetView(signalView);

        model.AddSignal(signalModel);

        // add button
        Button deleteSignal = createDeleteButton();
        deleteSignal.setUserData(model.GetNumSigs());
        buttonBox.getChildren().add(deleteSignal);

        // add the diagram
        HBox signalContainer = new HBox((signalView.GetDiagram()));
        signalContainer.setAlignment(Pos.CENTER_LEFT);
        signalContainer.setUserData(model.GetNumSigs());
        signalBox.getChildren().add(signalContainer);
        signalBox.setUserData(deleteSignal.getUserData());

        // Create event handlers for getting user input on the signal pane
        eventHandlerCreator.SetBounds(leftBound, rightBound);
        eventHandlerCreator.CreateEventHandlers(signalModel, signalView);
    }

    // Helpers: ---------------------------------------------------------
    private Button createDeleteButton() {
        Button b = new Button("X");
        b.setStyle(IDLE_BUTTON_STYLE);
        b.setOnMousePressed(e-> b.setStyle(PRESSED_BUTTON_STYLE));
        b.setOnMouseReleased(e-> b.setStyle(IDLE_BUTTON_STYLE));
        b.setOnAction((ActionEvent event) -> {
            removeSignal(b);
        });
        return b;
    }

    protected void removeSignal(Button b) {
        int userData = (int)b.getUserData();
        int index;

        // Find out which to remove
        HBox containerToRemove = (HBox)getByUserData(signalBox, userData);
        index = signalBox.getChildren().indexOf(containerToRemove);

        // Remove signal from view
        signalBox.getChildren().remove(containerToRemove);
        buttonBox.getChildren().remove(b);

        // Remove from model
        model.RemoveSignal(index);
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
