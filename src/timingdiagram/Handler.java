package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

abstract class Handler {
    protected final DSignal d_sig;

    Handler(DSignal d) {
        d_sig = d;
    }

    abstract public void handle(MouseEvent event);

    protected void draw_vertical(int coord) {
        d_sig.gc.beginPath();
        d_sig.gc.setStroke(Color.BLACK);
        d_sig.gc.setLineWidth(d_sig.line_width);
        d_sig.gc.moveTo(coord, d_sig.height);
        d_sig.gc.lineTo(coord, 0);
        d_sig.gc.stroke();
    }

    boolean in_between_edges(int coord) { // FIXME: not correct when direction changing from left to right
        boolean pos_empty = d_sig.pos_edges.size() == 0;
        boolean drawing_high = d_sig.h_line_position == DSignal.H_Position.HIGH;

        int cur_pos;
        int cur_neg;

        if (drawing_high) {
            if (pos_empty && edges_balanced()) { // means there is only a low signal
                return false;
            }
            if (edges_balanced()) { // closing edge only exists if edges are balanced
                for (int i = 0; i < d_sig.pos_edges.size(); i++) { // each i contains corresponding positive edge, then negative
                    cur_pos = d_sig.pos_edges.get(i);
                    cur_neg = d_sig.neg_edges.get(i);
                    if (cur_pos <= coord && coord <= cur_neg) {
                        return true;
                    }
                }
            }
        }
        else { // drawing low
            if (pos_empty && edges_balanced()) { // pos can be empty with edges unbalanced when erasing a positive edge
                return true;
            }
            if (edges_balanced()) {
                if (coord <= d_sig.pos_edges.get(0)) { // before first pos edge
                    return true;
                }
                for (int i = 0; i < d_sig.neg_edges.size(); i++) { // neg edge at i, then to close the low signal, corresponding pos is at i + 1
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
        }
        return false;
    }

    boolean edges_balanced() {
        return (d_sig.pos_edges.size() == d_sig.neg_edges.size());
    }
}
