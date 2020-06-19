package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.Collections;

public class MousePressHandler extends Handler {

    MousePressHandler(DSignal d) {
        super(d);
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            super.d_sig.previous_direction = DSignal.Direction.NULL;
            super.d_sig.h_line_position = DSignal.H_Position.HIGH;
//            super.d_sig.erase_edge = false;
            super.d_sig.initial_edge = (int)event.getX();
            super.d_sig.current_edge = (int) event.getX();
            if (!super.in_between_edges((int)event.getX())) {
                super.draw_vertical((int) event.getX());
                super.d_sig.pos_edges.add(super.d_sig.current_edge);
                Collections.sort(super.d_sig.pos_edges);
//                System.out.println("adding positive edge at: " + super.d_sig.current_edge);
            }
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            super.d_sig.previous_direction = DSignal.Direction.NULL;
            super.d_sig.h_line_position = DSignal.H_Position.LOW;
//            super.d_sig.erase_edge = false;
            super.d_sig.initial_edge = (int)event.getX();
            super.d_sig.current_edge = (int) event.getX();
            if (!super.in_between_edges((int)event.getX())) {
                super.draw_vertical((int) event.getX());
                super.d_sig.neg_edges.add(super.d_sig.current_edge);
                Collections.sort(super.d_sig.neg_edges);
//                System.out.println("adding negative edge at: " + super.d_sig.current_edge);
            }
        }
    }
}
