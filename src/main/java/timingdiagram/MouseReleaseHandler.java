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

        int release = (int)event.getX();

        if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
            if (super.d_sig.release_edge_pos) { // edge to be added is positive
                if (!super.in_low_signal(release) && !super.in_high_signal(release)) {
                    System.out.println("adding edge");
                    super.d_sig.pos_edges.add(release);
                    Collections.sort(super.d_sig.pos_edges);
                }
            }
            else { // edge to be added is negative
                    if (!super.in_high_signal(release) && !super.in_low_signal(release)) {
                        System.out.println("adding edge");
                        super.d_sig.neg_edges.add(release);
                        Collections.sort(super.d_sig.neg_edges);
                    }
            }
        }
    }
}
