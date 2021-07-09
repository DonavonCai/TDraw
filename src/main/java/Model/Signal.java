package Model;

import java.util.ArrayList;
import java.util.Objects;

/* A signal is a pair of Edges. The type of signal depends on the type of the left hand edge.
 * e.g. A (positive, negative) pair of edges is a high signal, and (negative, positive) is a low signal.
 *
 * Each signal has a set of children, which are other signals enclosed by the parent, forming a tree of signals, composing the diagram.
 * The immediate children of each signal must be of the opposite type to be valid.
 */
public class Signal {
    public enum Type {HIGH, LOW};

    // Data: -------------------------------------------------------------
    Type type;
    private Edge leftEdge;
    private Edge rightEdge;

    // Setters and getters: ----------------------------------------------
    public Type GetType() {
        return type;
    }

    public Edge GetLeftEdge() { return leftEdge; }

    public int GetLeftCoord() { return leftEdge.GetCoord(); }

    public Edge GetRightEdge() { return rightEdge; }

    public int GetRightCoord() { return rightEdge.GetCoord(); }

    public void SetLeftEdge(Edge left) { leftEdge = left; }

    public void SetRightEdge(Edge right) { rightEdge = right; }

    public void SetCoords(int left, int right) {
        leftEdge.SetCoord(left);
        rightEdge.SetCoord(right);
    }

    public void SetLeftCoord(int coord) {
        leftEdge.SetCoord(coord);
    }

    public void SetRightCoord(int coord) {
        rightEdge.SetCoord(coord);
    }

    public void SetLeftType(Edge.Type t) { leftEdge.SetType(t); }

    public void SetRightType(Edge.Type t) { rightEdge.SetType(t); }

    // Constructors: -----------------------------------------------------
    public Signal() {
        leftEdge = rightEdge = null;
    }

    // copy constructor. use 'new' for non-primitives
    public Signal(Signal s) {
        leftEdge = new Edge(s.GetLeftEdge());
        rightEdge = new Edge(s.GetRightEdge());
        type = s.type;
    }

    public Signal(int c) {
        leftEdge = new Edge(c);
        rightEdge = new Edge(c);
    }

    public Signal(int left, int right) {
        leftEdge = new Edge(left);
        rightEdge = new Edge(right);
    }

    public Signal(int left, int right, Type t) {
        leftEdge = new Edge(left);
        rightEdge = new Edge(right);
        SetType(t);
    }

    public Signal(Edge left) {
        leftEdge = left;
        type = (left.GetType() == Edge.Type.POS)? Type.HIGH : Type.LOW;
        rightEdge = null;
    }

    public Signal(Edge left, Edge right) {
        leftEdge = left;
        rightEdge = right;
        type = (left.GetType() == Edge.Type.POS)? Type.HIGH : Type.LOW;
    }

    // Interface: --------------------------------------------------------

    // Sets this.type, also assigns types to left and right edges accordingly
    public void SetType(Type t) {
        if (t == Signal.Type.HIGH) {
            leftEdge.SetType(Edge.Type.POS);
            rightEdge.SetType(Edge.Type.NEG);
        }
        else {
            leftEdge.SetType(Edge.Type.NEG);
            rightEdge.SetType(Edge.Type.POS);
        }
    }

    public void SwapEdges() {
        Edge temp = leftEdge;
        leftEdge = rightEdge;
        rightEdge = temp;
    }

    public boolean Encloses(int coord) {
        assert leftEdge != null;
        assert rightEdge != null;
        assert leftEdge.GetCoord() <= rightEdge.GetCoord();
        return (leftEdge.GetCoord() <= coord && coord <= rightEdge.GetCoord());
    }

    public boolean Encloses(Signal s) {
        assert leftEdge != null;
        assert rightEdge != null;
        assert leftEdge.GetCoord() <= rightEdge.GetCoord();
        return (this.leftEdge.GetCoord() <= s.GetLeftCoord() && s.GetRightCoord() <= this.rightEdge.GetCoord());
    }

    public boolean RightOf(Signal s) {
        assert s != null;
        assert leftEdge != null;
        assert rightEdge != null;

        return (this.leftEdge.GetCoord() >= s.GetRightCoord());
    }

    @Override
    public boolean equals(Object obj) {
        boolean result = false;
        if (obj != null && obj instanceof Signal) {
            result = Objects.equals(this, obj);
        }
        return result;
    }
}
