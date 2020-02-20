package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

abstract class Handler {
    protected DSignal d_sig;

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

    protected void h_line_flip() {
        if (d_sig.h_line_position == DSignal.H_Position.HIGH) {
            d_sig.h_line_position = DSignal.H_Position.LOW;
        }
        else if (d_sig.h_line_position == DSignal.H_Position.LOW) {
            d_sig.h_line_position = DSignal.H_Position.HIGH;
        }
    }

    boolean in_between_edges(int coord) { // TODO: fix this
        boolean pos_empty = d_sig.pos_edges.size() == 0;
        boolean neg_empty = d_sig.neg_edges.size() == 0;
        boolean drawing_high = d_sig.h_line_position == DSignal.H_Position.HIGH;
        boolean drawing_low = d_sig.h_line_position == DSignal.H_Position.LOW;

        if (pos_empty && neg_empty) { // no edges
            if (drawing_low) {
                System.out.println("neg edge on initial");
                return true;
            }
            return false;
        }
        else {
            if (drawing_low && !pos_empty && coord < d_sig.pos_edges.get(0) && !d_sig.moving_backwards) { // low signal before first positive edge, !moving_backwards so you can drag left initially
                System.out.println("low sig before 1st pos edge");
                return true;
            }
            for (int i = 0; (i < d_sig.pos_edges.size() && (i < d_sig.neg_edges.size())); i++) {
                if (drawing_high && edges_balanced()) { // avoid the case where we are "in between" because we haven't finished adding an edge through dragging
                    if ((coord >= d_sig.pos_edges.get(i)) && (coord <= d_sig.neg_edges.get(i))) { // positive edge placement on high signal
                        System.out.println("pos edge on high signal: in between " + d_sig.pos_edges.get(i) + "," + d_sig.neg_edges.get(i));
                        return true;
                    }
                }
                else if (drawing_low && edges_balanced()){
                    if ((coord >= d_sig.neg_edges.get(i)) && (coord <= d_sig.pos_edges.get(i))) { // negative edge placement on low signal
                        System.out.println("neg edge on low signal: in between " + d_sig.pos_edges.get(i) + "," + d_sig.neg_edges.get(i));
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
