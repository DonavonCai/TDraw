package Controller;

import Model.DiagramModel;
import javafx.fxml.FXML;

import View.View;

public class TDrawController {
    // Members: ------------------------------------------------------
    DiagramModel model;

    @FXML
    SignalController signalController;

    // Functions used in FXML: ---------------------------------------
    @FXML
    public void AddSignal() {
        signalController.AddSignal();
    }

    @FXML
    public void initialize() {

    }

    // Functions: ----------------------------------------------------

    public void SetModel(DiagramModel m) {
        model = m;
    }

//    public void PassViewToDiagram(View view) {
//        model.SetView(view);
//    }
}
