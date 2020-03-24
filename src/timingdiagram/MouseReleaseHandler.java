package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.Collections;
import java.util.ArrayList;

public class  MouseReleaseHandler extends Handler {

    MouseReleaseHandler(DSignal d) {
        super(d);
    }

    @Override
    public void handle(MouseEvent event) {
        if (in_between_edges((int)event.getX())) {
            return;
        }
        super.d_sig.previous_direction = DSignal.Direction.NULL;
        super.d_sig.current_direction = DSignal.Direction.NULL;
        super.d_sig.initial_direction = DSignal.Direction.NULL;
        super.d_sig.prev_mouse_coord = -1;

        int release = (int)event.getX();

        if (event.getButton() == MouseButton.PRIMARY || event.getButton() == MouseButton.SECONDARY) {
            if (super.d_sig.initial_edge_is_neg) { // if initial edge is neg, closing edge is pos
                super.d_sig.pos_edges.add(release);
                Collections.sort(super.d_sig.pos_edges);
                System.out.println("release: adding pos edge at: " + event.getX() + ", " + event.getY());
            }
            else {
                super.d_sig.neg_edges.add(release);
                Collections.sort(super.d_sig.neg_edges);
                System.out.println("release: adding neg edge at: " + event.getX() + ", " + event.getY());
            }
        }

        System.out.println(super.d_sig.pos_edges.size() + " pos edges");
        System.out.println(super.d_sig.neg_edges.size() + " neg edges");
        System.out.println("first pos: " + super.d_sig.pos_edges.get(0));
        System.out.println("first neg: " + super.d_sig.neg_edges.get(0));
    }
}
