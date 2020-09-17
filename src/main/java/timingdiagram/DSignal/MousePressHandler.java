package timingdiagram.DSignal;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.io.Serializable;

import java.util.Collections;

class MousePressHandler extends Handler {

    MousePressHandler(DSignal d, DirectionTracker t) {
        super(d, t);
    }

    @Override
    public void handle(MouseEvent event) {
        int coord = (int)event.getX();
        if (event.getButton() == MouseButton.PRIMARY) {
            super.directionTracker.clear_prevDirection();
            super.directionTracker.set_drawhigh();
            super.d_sig.initial_edge = coord;
            super.d_sig.current_edge = coord;
            if (!super.in_high_signal(coord)) {
                super.draw_vertical(coord);
                super.d_sig.pos_edges.add(super.d_sig.current_edge);
                Collections.sort(super.d_sig.pos_edges);
            }
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            super.directionTracker.clear_prevDirection();
            super.directionTracker.set_drawlow();
            super.d_sig.initial_edge = coord;
            super.d_sig.current_edge = coord;
            if (!super.in_low_signal(coord)) {
                super.draw_vertical(coord);
                super.d_sig.neg_edges.add(super.d_sig.current_edge);
                Collections.sort(super.d_sig.neg_edges);
            }
        }
    }
}
