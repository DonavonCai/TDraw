package Controller;

import Model.DiagramModel;
import javafx.fxml.FXML;

import View.View;

public class TDrawController {
    DiagramModel model;

    @FXML
    public void AddSubDiagram() {
        System.out.println("Add signal");
    }

    public void SetModel(DiagramModel m) {
        model = m;
    }

    public void PassViewToDiagram(View view) {
        model.SetView(view);
    }
}
