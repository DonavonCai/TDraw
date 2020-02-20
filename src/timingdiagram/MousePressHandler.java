package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.Collections;

public class MousePressHandler {
    private DSignal d_sig;

    MousePressHandler(DSignal d) {
        d_sig = d;
    }

    protected void handle(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            d_sig.previous_direction = DSignal.Direction.NULL;
            d_sig.h_line_position = DSignal.H_Position.HIGH;
            d_sig.erase_edge = false;
            if (!d_sig.in_between_edges((int)event.getX())) {
                d_sig.draw_vertical((int) event.getX());
                d_sig.current_edge = (int) event.getX();
                d_sig.pos_edges.add(d_sig.current_edge);
                Collections.sort(d_sig.pos_edges);
                d_sig.click_edge_to_add = d_sig.current_edge;
                System.out.println("adding positive edge at: " + d_sig.current_edge);
            }
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            d_sig.previous_direction = DSignal.Direction.NULL;
            d_sig.h_line_position = DSignal.H_Position.LOW;
            d_sig.erase_edge = false;
            if (!d_sig.in_between_edges((int)event.getX())) {
                d_sig.draw_vertical((int) event.getX());
                d_sig.current_edge = (int) event.getX();
                d_sig.neg_edges.add(d_sig.current_edge);
                Collections.sort(d_sig.neg_edges);
                d_sig.click_edge_to_add = d_sig.current_edge;
                System.out.println("adding negative edge at: " + d_sig.current_edge);
            }
        }
    }
}
