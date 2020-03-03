package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.Collections;

public class MouseReleaseHandler extends Handler {
    MouseReleaseHandler(DSignal d) {
        super(d);
    }

    @Override
    public void handle(MouseEvent event) {
        if (in_between_edges((int)event.getX())) {
            return;
        }
        super.d_sig.moving_backwards = false;
        super.d_sig.previous_direction = DSignal.Direction.NULL;
        super.d_sig.current_direction = DSignal.Direction.NULL;
        super.d_sig.initial_direction = DSignal.Direction.NULL;
        super.d_sig.prev_mouse_coord = -1;
        if (event.getButton() == MouseButton.PRIMARY) {
//                            super.d_sig.pos_edges.add(super.d_sig.click_edge_to_add);
//                            Collections.sort(super.d_sig.pos_edges);
            super.d_sig.neg_edges.add((int)event.getX());
            Collections.sort(super.d_sig.neg_edges);
            System.out.println("release: adding neg edge at: " + event.getX() + ", " + event.getY());
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
//                            super.d_sig.neg_edges.add(super.d_sig.click_edge_to_add);
//                            Collections.sort(super.d_sig.neg_edges);
            super.d_sig.pos_edges.add((int)event.getX());
            Collections.sort(super.d_sig.pos_edges);
            System.out.println("release: adding pos edge at: " + event.getX() + ", " + event.getY());
        }
    }
}
