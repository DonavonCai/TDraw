package Model;

import java.util.ArrayList;
import java.util.Collections;

public class Signal {
    public enum Type {HIGH, LOW};

    private int pivot;
    private int leftBound;
    private int rightBound;
    private ArrayList<Edge> edges;

    // Setters and getters: ---------------------------
    public ArrayList<Edge> GetEdges() {
        return edges;
    }

    public void SetPivot(int coord) {
        pivot = coord;
    }

    // Constructors: ----------------------------------
    public Signal() {
        edges = new ArrayList<Edge>();
    }

    public Signal(ArrayList<Edge> arr) {
        edges = arr;
    }

    // Interface: -------------------------------------
    public void Init(Edge left, Edge right) {
        assert edges != null;
        leftBound = left.GetCoord();
        rightBound = right.GetCoord();
        edges.add(left);
        edges.add(right);
        // todo: sort?
    }

    public void Create(Signal.Type signalType, int c) {
        Edge e = new Edge(c);
        e.SetType(LeftEdgeType(signalType));

        SetPivot(c);
        InsertEdge(e);
    }

    // Extend the signal from pivot to coord.
    // If any edges are in-between pivot and coord, delete them.
    public void Extend(int coord) {
        // (exclusive, exclusive) range delete
        DeleteEdgesInRange(pivot, coord);

        // If an edge already exists at coord, stop
        if (EdgesIndexOf(coord) >= 0) {
            return;
        }

        int pivotIdx = EdgesIndexOf(pivot);
        assert pivotIdx >= 0;

        Edge.Type type = edges.get(pivotIdx).GetType().opposite();
        InsertEdge(new Edge(coord, type));
    }

    // Sets the pivot edge's type to the opposite type
    public void Flip() {
        int pivotIdx = EdgesIndexOf(pivot);
        assert pivotIdx >= 0;

        Edge.Type newType = edges.get(pivotIdx).GetType().opposite();
        edges.get(pivotIdx).SetType(newType);
    }

    // Helpers: -----------------------------------------
    private Edge.Type LeftEdgeType(Signal.Type t) {
        Edge.Type result;
        result = (t == Signal.Type.HIGH)? Edge.Type.POS : Edge.Type.NEG;
        return result;
    }

    private Edge.Type RightEdgeType(Signal.Type t) {
        Edge.Type result;
        result = (t == Signal.Type.HIGH)? Edge.Type.NEG : Edge.Type.POS;
        return result;
    }

    private void InsertEdge(Edge e) {
        edges.add(e);
        Collections.sort(edges);
    }

    private void InsertEdges(Edge left, Edge right) {
        edges.add(left);
        edges.add(right);
        Collections.sort(edges);
    }

    // Deletiong is exclusive on both a and b
    private void DeleteEdgesInRange(int a, int b) {
        if (a > b) {
            int c = 0;
            c=a;
            a=b;
            b=c;
        }
        for (int i = 0; i < edges.size(); i++) {
            int cur = edges.get(i).GetCoord();
            if (a < cur && cur < b) {
                edges.remove(i);
                i--;
            }
        }
    }

    // Gets index element in edges with coord that matches target.
    // If no such element is found, returns -1.
    private int EdgesIndexOf(int target) {
        int i;
        for (i = 0; i < edges.size(); i++) {
            int cur = edges.get(i).GetCoord();
            if (cur == target) {
                return i;
            }
        }
        return -1;
    }
}
