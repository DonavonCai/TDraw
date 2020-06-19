package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.Collections;

public class  MouseReleaseHandler extends Handler {

    MouseReleaseHandler(DSignal d) {
        super(d);
    }

    @Override
    public void handle(MouseEvent event) { // note: must reset fields before returning
        super.d_sig.previous_direction = DSignal.Direction.NULL;
        super.d_sig.current_direction = DSignal.Direction.NULL;
        super.d_sig.initial_direction = DSignal.Direction.NULL;
        super.d_sig.prev_mouse_coord = -1;

        if (in_between_edges((int)event.getX())) {
            return;
        }

        int release = (int)event.getX();

        if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
            if (super.d_sig.release_edge_pos) {
                super.d_sig.pos_edges.add(release);
                Collections.sort(super.d_sig.pos_edges);
            }
            else {
                super.d_sig.neg_edges.add(release);
                Collections.sort(super.d_sig.neg_edges);
            }
        }
    }
}
