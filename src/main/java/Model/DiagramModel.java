package Model;

import Model.Helper.SignalTree;

import View.View;

// This class provides the interface that the controller will use to update the model.

public class DiagramModel {
    private View view;
    private SignalTree tree;

    public void SetView(View v) {
        view = v;
    }

    public void SetTree(SignalTree t) {
        tree = t;
    }

    public DiagramModel() {

    }

    public void CreateSignal(int c) {
//        tree.CreateSignal(c);
    }

    public void ExtendSignal(int c) {

    }

    public void FinishSignal() {

    }

    public void NotifyView() {

    }
}
