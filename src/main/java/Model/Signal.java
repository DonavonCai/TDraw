package Model;

import java.util.ArrayList;
import java.util.Collections;

public class Signal {
    public enum Type {HIGH, LOW};

    private Edge pivot;
    private Edge extendEdge;
    private int leftBound;
    private int rightBound;
    private ArrayList<Edge> edges;

    // Setters and getters: ---------------------------
    public ArrayList<Edge> GetEdges() {
        return edges;
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
        assert edges.size() == 0;
        leftBound = left.GetCoord();
        rightBound = right.GetCoord();
        edges.add(left);
        edges.add(right);
        Collections.sort(edges);
    }


    public void Anchor(Signal.Type signalType, int c) {
        pivot = new Edge(c);
        extendEdge = new Edge(Integer.MIN_VALUE);
        // Assume we will drag right
        pivot.SetType(LeftEdgeType(signalType));
        extendEdge.SetType(pivot.GetType().opposite());

        if (!exists(pivot) && OppositeTypes(pivot, LeftNeighbor(pivot))) {
            InsertEdge(pivot);
        }
    }

    // Extend the signal from pivot to coord.
    // If any edges are in-between pivot and coord, delete them.
    public void Extend(int coord) {
        DeleteEdgesInRange(pivot.GetCoord(), coord);
        extendEdge.SetCoord(coord);

        // If an edge already exists at coord, stop
        if (IndexOfCoord(coord) >= 0) {
            return;
        }
        
        // Find out if extendEdge is valid to add
        Edge compareLeft = LeftNeighbor(extendEdge);
        Edge compareRight = RightNeighbor(extendEdge);

        if (OppositeTypes(compareLeft, extendEdge) && OppositeTypes(extendEdge, compareRight)) {
            ExtendEdgeTo(coord);
        }
    }

    // Sets the pivot edge's type to the opposite type
    public void SetDirectionLeft() {
        // Flip types for pivot and extendEdge
        pivot.SetType(pivot.GetType().opposite());
        extendEdge.SetType(extendEdge.GetType().opposite());

        // Copy those changes to edges arr if present
        int pivotIdx = IndexOfCoord(pivot.GetCoord());
        if (pivotIdx > -1) {
            edges.remove(pivotIdx);
            edges.add(pivot);
        }
        int extIdx = IndexOfCoord(extendEdge.GetCoord());
        if (extIdx > -1) {
            edges.remove(extIdx);
            edges.add(extendEdge);
        }
        Collections.sort(edges);
    }

    public void PrintEdges() {
        System.out.println("========================");
        for (int i = 0; i < edges.size(); i++) {
            System.out.println(i + ": " + edges.get(i).GetType() + ", " + edges.get(i).GetCoord());
        }
        System.out.println("========================");
    }

    // Helpers: -----------------------------------------
    private void ExtendEdgeTo(int coord) {
        if (!exists(extendEdge)) { // First tick
            extendEdge.SetCoord(coord);
            InsertEdge(extendEdge);
        }
        else { // Past first tick
            int idx = edges.indexOf(extendEdge);
            edges.get(idx).SetCoord(coord);
            extendEdge = edges.get(idx);
        }
    }

    private Edge.Type LeftEdgeType(Signal.Type t) {
        Edge.Type result;
        if (t == Signal.Type.HIGH) {
            result = Edge.Type.POS;
        }
        else {
            result = Edge.Type.NEG;
        }
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
    private int IndexOfCoord(int target) {
        int i;
        for (i = 0; i < edges.size(); i++) {
            int cur = edges.get(i).GetCoord();
            if (cur == target) {
                return i;
            }
        }
        return -1;
    }

    // Get the closest edge left of E.
    private Edge LeftNeighbor(Edge e) {
        Edge cur;
        for (int i = edges.size() - 1; i >= 0; i--) {
            cur = edges.get(i);
            if (cur.GetCoord() < e.GetCoord()) {
                return cur;
            }
        }
        return null;
    }

    private Edge RightNeighbor(Edge e) {
        Edge cur;
        for (int i = 0; i < edges.size(); i++) {
            cur = edges.get(i);
            if (cur.GetCoord() > e.GetCoord()) {
                return cur;
            }
        }
        return null;
    }

    private boolean SameTypes(Edge a, Edge b) {
        return a.GetType() == b.GetType();
    }

    private boolean OppositeTypes(Edge a, Edge b) {
        return a.GetType() != b.GetType();
    }

    private boolean exists(Edge e) {
        return edges.indexOf(e) > -1;
    }
}
