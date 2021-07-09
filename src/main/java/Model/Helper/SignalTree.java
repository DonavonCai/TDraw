package Model.Helper;

import java.util.ArrayList;

import Model.Signal;

// This class is an expert that DiagramModel defers to when adding and deleting edges.
// The tree is organized in the same way as a parentheses tree.
// Each parent node encloses its children.
public class SignalTree {
    // The root is the outermost pair of edges, located at the beginning and end of the diagram.
    private SignalNode root;

    public SignalTree() {

    }

    public SignalTree(SignalNode r) {
        root = r;
    }

    public void AddSignal(int c) {
        SignalNode node = new SignalNode(new Signal(c));
        root.Insert(node);
    }

    public void ExtendSignal(int c) {

    }

    public void FinishSignal() {

    }

    public ArrayList<SignalNode> GetNodes() {
        return null;
    }

    public ArrayList<Integer> GetEdgeCoords() {
        ArrayList<Integer> arr = root.CoordsInOrder();
        return arr;
    }
}
