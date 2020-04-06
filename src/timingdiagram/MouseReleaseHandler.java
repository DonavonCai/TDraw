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
//                System.out.println("release: adding pos edge at: " + event.getX());
            }
            else {
                super.d_sig.neg_edges.add(release);
                Collections.sort(super.d_sig.neg_edges);
//                System.out.println("release: adding neg edge at: " + event.getX());
            }
        }
//        print_edges();
    }

    private void print_edges() {
//        System.out.println(super.d_sig.pos_edges.size() + " pos edges:");
        for (int i = 0; i < super.d_sig.pos_edges.size(); i++) {
            System.out.println(super.d_sig.pos_edges.get(i));
        }

//        System.out.println(super.d_sig.neg_edges.size() + " neg edges:");
        for (int i = 0; i < super.d_sig.neg_edges.size(); i++) {
            System.out.println(super.d_sig.neg_edges.get(i));
        }
    }
}
