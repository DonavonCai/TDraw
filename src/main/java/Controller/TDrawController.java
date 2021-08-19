package Controller;

import Controller.MenuController.FileMenuController;
import Model.DiagramModel;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class TDrawController {
    // Members: ------------------------------------------------------
    private DiagramModel model;

    // injected from launcher class
    private Stage primaryStage;

    // Setters and getters: ------------------------------------------
    public void SetModel(DiagramModel m) {
        model = m;
    }
    public void SetPrimaryStage(Stage p) {
        primaryStage = p;
    }

    // Nested Controllers: -------------------------------------------
    @FXML
    private SignalController signalController;
    @FXML
    private FileMenuController fileMenuController;

    // Functions used in FXML: ---------------------------------------
    @FXML
    public void AddSignal() {
        signalController.AddSignal();
    }

    @FXML
    public void initialize() {
        fileMenuController.SetSignalBox(signalController.GetSignalBox());
        fileMenuController.SetPrimaryStage(primaryStage);
    }
}
