package Model.Helper;

import Model.Signal;

import java.util.ArrayList;
import java.util.Collections;

// Wrapper class used by SignalTreeManager.
public class SignalNode {
    // Data: ------------------------------------------------------
    public Signal signal;
    // Children only includes nodes exactly 1 level below (i.e. this node is the immediate signal enclosing the child).
    private ArrayList<SignalNode> children;

    // Constructors: ----------------------------------------------
    public SignalNode(int c) {
        signal = new Signal(c);
        children = new ArrayList<SignalNode>();
    }
    public SignalNode(Signal s) {
        signal = s;
        children = new ArrayList<SignalNode>();
    }

    // Setters and Getters: ---------------------------------------
    public Signal GetSignal() { return signal; }

    public ArrayList<SignalNode> GetChildren() { return children; }

    public void SetChildren(ArrayList<SignalNode> c) {
        children = c;
    }

    // Recursive traversal of tree in order
    public ArrayList<Integer> CoordsInOrder() {
        assert children != null;
        ArrayList<Integer> arr = new ArrayList<Integer>();

        // Add left edge
        arr.add(signal.GetLeftCoord());

        // Add children
        for (int i = 0; i < children.size(); i++) {
            ArrayList<Integer> child = children.get(i).CoordsInOrder();
            for (int j = 0; j < child.size(); j++) {
                arr.add(child.get(j));
            }
        }

        // Add right edge
        arr.add(signal.GetRightCoord());

        // return
        return arr;
    }

    // Interface: -------------------------------------------------
    // Check if any children enclose the input. If so, recursively insert into the child.
    // Else, assuming this node encloses the input, add to this.children.
    public void Insert(SignalNode s) {
        // Recursive case: offload insertion to children if possible
        assert children != null;
        for (int i = 0; i < children.size(); i++) {
            SignalNode cur = children.get(i);
            if (cur.ValidChild(s)) {
                children.get(i).Insert(s);
                return;
            }
        }
        // Escape condition: try adding to this node's children
        if (this.ValidChild(s)) {
            System.out.println("base case");
            children.add(s);
            SortChildren();
        }
    }

    public boolean ValidChild(SignalNode s) {
        assert s.GetSignal().GetLeftEdge() != null;
        assert s.GetSignal().GetRightEdge() != null;
        return signal.Encloses(s.GetSignal());
    }

    public boolean Encloses(SignalNode s) {
        return signal.Encloses(s.GetSignal());
    }

    // Helpers: --------------------------------------------------
    private void SortChildren() {
        int n = children.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                Signal cur = children.get(j).GetSignal();
                Signal next = children.get(j + 1).GetSignal();
                if (cur.RightOf(next)) {
                    Collections.swap(children, j, j + 1);
                }
            }
        }
    }
}
