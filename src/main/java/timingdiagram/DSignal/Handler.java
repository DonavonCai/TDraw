package timingdiagram.DSignal;

import javafx.scene.input.MouseEvent;

import java.io.Serializable;

abstract class Handler implements Serializable {
    protected DSignal d_sig;
    protected DirectionTracker directionTracker;

    Handler(DSignal d, DirectionTracker t) {
        d_sig = d;
        directionTracker = t;
    }

    abstract public void handle(MouseEvent event);

    protected void draw_vertical(int coord) {
        d_sig.draw_vertical(coord);
    }

    protected DirectionTracker.Direction getCurrentDirection() {
        return directionTracker.current_direction;
    }

    // returns true if coord is in between 2 pairs of edges that form a high signal
    boolean in_high_signal(int coord) {
        boolean pos_empty = d_sig.pos_edges.size() == 0;

        int cur_pos;
        int cur_neg;

        if (pos_empty && edges_balanced()) { // means there is only a low signal
            return false;
        }
        if (edges_balanced()) { // closing edge only exists if edges are balanced
            for (int i = 0; i < d_sig.pos_edges.size(); i++) { // find the pair of edges surrounding the edge to be added
                cur_pos = d_sig.pos_edges.get(i);
                cur_neg = d_sig.neg_edges.get(i);
                if (cur_pos <= coord && coord <= cur_neg) {
                    return true;
                }
            }
        }
        return false;
    }

    // returns true if coord is in the middle of a low signal
    boolean in_low_signal(int coord) {
        boolean pos_empty = d_sig.pos_edges.size() == 0;

        int cur_pos;
        int cur_neg;
        if (pos_empty && edges_balanced()) { // pos can be empty with edges unbalanced when erasing a positive edge
            return true;
        }
        if (edges_balanced()) {
            if (coord <= d_sig.pos_edges.get(0)) { // before first pos edge
                return true;
            }
            for (int i = 0; i < d_sig.neg_edges.size(); i++) { // neg edge at i, corresponding pos edge is at i + 1
                cur_neg = d_sig.neg_edges.get(i);
                if (coord >= cur_neg) {
                    if (i + 1 >= d_sig.pos_edges.size()) { // after last negative edge
                        return true;
                    }
                    cur_pos = d_sig.pos_edges.get(i + 1);
                    if (coord <= cur_pos) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean edges_balanced() {
        return (d_sig.pos_edges.size() == d_sig.neg_edges.size());
    }
}
