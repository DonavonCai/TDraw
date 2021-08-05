package TimingDiagram.DSignal;

import javafx.scene.input.MouseEvent;
import TimingDiagram.DSignal.Edge.Edge;

import java.io.Serializable;

abstract class Handler implements Serializable {
    protected DSignal d_sig;
    protected DirectionTracker directionTracker;
    protected DragBoundsTracker dragBoundsTracker;

    protected enum EdgeOrigin {
        PRESS,
        RELEASE,
        DRAG
    }

    Handler(DSignal d, DirectionTracker t, DragBoundsTracker t2) {
        d_sig = d;
        directionTracker = t;
        dragBoundsTracker = t2;
    }

    abstract public void handle(MouseEvent event);

    protected void draw_vertical(double coord) {
        d_sig.draw_vertical(coord);
    }

    /* From d_sig.edges, returns index of the first edge
     * whose coordinate is greater than coord */
    protected int first_idx_greater_than(int coord) {
        for (int i = 0; i < d_sig.edges.size(); i++) {
            if (d_sig.edges.get(i).getCoord() > coord) {
                return i;
            }
        }
        return -1;
    }

    /* From d_sig.edges, returns index of the last edge
    * whose coordinate is less than coord */
    protected int last_idx_less_than(int coord) {
        int r = -1;
        for (int i = 0; i < d_sig.edges.size(); i++) {
            if (d_sig.edges.get(i).getCoord() < coord) {
                r = i;
            }
        }
        return r;
    }

    // returns true if coord is in between 2 pairs of edges that form a high signal
    boolean in_high_signal(double coord) {
        Edge leftEdge, rightEdge;
        // Loop through edges
        for (int i = 0; i < d_sig.edges.size() - 1; i++) {
            // get a positive edge, pair it with the next edge
            if (d_sig.edges.get(i).getType() == Edge.Type.POS) {
                leftEdge = d_sig.edges.get(i);
                rightEdge = d_sig.edges.get(i + 1);

                if (leftEdge.getCoord() <= coord && coord <= rightEdge.getCoord()) {
                    return true;
                }
            }
        }
        return false;
    }

    Edge leftNeighbor(int coord) {
        int idx = last_idx_less_than((int)coord);
        if (idx < 0)
            return null;

        return d_sig.edges.get(idx);
    }

    Edge.Type leftNeighborType(double coord) {
        int idx = last_idx_less_than((int)coord);
        if (idx < 0)
            return null;

        return d_sig.edges.get(idx).getType();
    }

    Edge.Type leftNeighborType(Edge e) {
        int idx = last_idx_less_than((int)e.getCoord());
        if (idx < 0)
            return null;

        return d_sig.edges.get(idx).getType();
    }

    Edge rightNeighbor(int coord) {
        int idx = first_idx_greater_than(coord);
        if (idx < 0)
            return null;

        return d_sig.edges.get(idx);
    }

    Edge.Type rightNeighborType(double coord) {
        int idx = first_idx_greater_than((int)coord);
        if (idx < 0)
            return null;

        return d_sig.edges.get(idx).getType();
    }

    Edge.Type rightNeighborType(Edge e) {
        int idx = first_idx_greater_than((int)e.getCoord());
        if (idx < 0)
            return null;

        return d_sig.edges.get(idx).getType();
    }
}
