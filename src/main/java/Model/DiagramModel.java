package Model;

import java.util.ArrayList;

// This class provides the interface that the controller will use to update the model.

//todo: array of contexts for signals. put the array in View or this class?
public class DiagramModel {
    // Constants: ----------------------------------------------------
    int MAX_SIGS = 10;

    // Members: ------------------------------------------------------
    int numSigs;
    private ArrayList<Signal> signalModels;

    // Setters and Getters: ------------------------------------------
    public int GetNumSigs() { return numSigs; }

    // Interface: ----------------------------------------------------
    public DiagramModel() {
        numSigs = 0;
        signalModels = new ArrayList<Signal>();
    }

    public void AddSignal(Signal signalModel) {
        if (numSigs < MAX_SIGS) {
            numSigs++;
            signalModels.add(signalModel);
        }
    }

    // fixme: removes in wrong order
    public void RemoveSignal(int i) {
        if (numSigs > 0) {
            numSigs--;
            signalModels.remove(i);
        }
    }
}
