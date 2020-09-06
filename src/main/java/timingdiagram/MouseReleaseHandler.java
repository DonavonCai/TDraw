package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.Collections;

public class  MouseReleaseHandler extends Handler {

    MouseReleaseHandler(DSignal d, DirectionTracker t) {
        super(d, t);
    }

    @Override
    public void handle(MouseEvent event) {
        super.directionTracker.reset();

        if (in_between_edges((int)event.getX())) {
            return;
        }
        System.out.println("adding edge");

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
