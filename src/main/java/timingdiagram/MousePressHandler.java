package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.Collections;

public class MousePressHandler extends Handler {

    MousePressHandler(DSignal d, DirectionTracker t) {
        super(d, t);
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            super.directionTracker.clear_prevDirection();
            super.directionTracker.set_drawhigh();
            super.d_sig.initial_edge = (int)event.getX();
            super.d_sig.current_edge = (int) event.getX();
            if (!super.in_between_edges((int)event.getX())) {
                super.draw_vertical((int) event.getX());
                super.d_sig.pos_edges.add(super.d_sig.current_edge);
                Collections.sort(super.d_sig.pos_edges);
            }
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            super.directionTracker.clear_prevDirection();
            super.directionTracker.set_drawlow();
            super.d_sig.initial_edge = (int)event.getX();
            super.d_sig.current_edge = (int) event.getX();
            if (!super.in_between_edges((int)event.getX())) {
                super.draw_vertical((int) event.getX());
                super.d_sig.neg_edges.add(super.d_sig.current_edge);
                Collections.sort(super.d_sig.neg_edges);
            }
        }
    }
}
