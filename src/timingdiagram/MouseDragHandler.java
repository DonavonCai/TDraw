package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import java.util.Collections;

public class MouseDragHandler extends Handler {

    MouseDragHandler(DSignal d) {
        super(d);
    }

    @Override
    public void handle(MouseEvent event) {
//                        System.out.println("initial direction: " + initial_direction);
        // get mouse direction
        if (super.d_sig.prev_mouse_coord > 0 && (int)event.getX() < super.d_sig.prev_mouse_coord) { // moving left
            if (super.d_sig.initial_direction == DSignal.Direction.NULL) { // set initial direction
                super.d_sig.initial_direction = DSignal.Direction.LEFT;
            }
            super.d_sig.current_direction = DSignal.Direction.LEFT;
        }
        else if (super.d_sig.prev_mouse_coord > 0 && (int)event.getX() > super.d_sig.prev_mouse_coord) { // moving right
            if (super.d_sig.initial_direction == DSignal.Direction.NULL) {
                super.d_sig.initial_direction = DSignal.Direction.RIGHT;
            }
            super.d_sig.current_direction = DSignal.Direction.RIGHT;
        }

        if (super.d_sig.current_direction != super.d_sig.previous_direction && super.d_sig.previous_direction != DSignal.Direction.NULL) { // direction change
//            System.out.println("direction change");
            super.d_sig.current_edge = (int)event.getX();

            if (!in_between_edges(super.d_sig.current_edge)) {
//                System.out.println("dir change: adding edge at: " + super.d_sig.current_edge);
                if (event.getButton() == MouseButton.PRIMARY) {
                    super.d_sig.neg_edges.add(d_sig.current_edge);
                    Collections.sort(super.d_sig.neg_edges);
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    super.d_sig.pos_edges.add(d_sig.current_edge);
                    Collections.sort(super.d_sig.pos_edges);
                }
            }
        }
        if (!super.in_between_edges((int)event.getX())) { // this vertical line is drawn over in draw_horizontal if mouse continues to be dragged
            super.draw_vertical((int)event.getX());
            draw_horizontal((int) event.getX(), super.d_sig.current_edge, super.d_sig.current_direction);
        }
        super.d_sig.previous_direction = super.d_sig.current_direction;
        super.d_sig.prev_mouse_coord = (int)event.getX();
    }

    protected void draw_horizontal(int coord, int respective_edge, DSignal.Direction current_direction) { // TODO: only draw vertical if applicable
        boolean draw_high = (super.d_sig.h_line_position == DSignal.H_Position.HIGH);

        super.d_sig.gc.beginPath();
        super.d_sig.gc.setStroke(Color.BLACK);
        super.d_sig.gc.setLineWidth(super.d_sig.line_width);

        if (draw_high) {
            super.d_sig.gc.moveTo(respective_edge, 0);
            super.d_sig.gc.lineTo(coord, 0);
            super.d_sig.gc.stroke();
        }
        else {
            super.d_sig.gc.moveTo(respective_edge, super.d_sig.height);
            super.d_sig.gc.lineTo(coord, super.d_sig.height);
            super.d_sig.gc.stroke();
        }

        // erase signal
        int rect_x;
        int rect_y;
        int rect_width;
        int rect_height;

        super.d_sig.gc.setFill(Color.WHITE);
        if (draw_high) {
            rect_y = super.d_sig.line_width - 1;
            rect_height = super.d_sig.height;
            if (current_direction == DSignal.Direction.LEFT) { // erase right
                rect_x = coord;
                rect_width = respective_edge - coord;
            }
            else { // erase left
                rect_x = respective_edge;
                rect_width = coord - respective_edge;
            }
        }
        else { // draw low
            rect_y = 0;
            rect_height = super.d_sig.height - super.d_sig.line_width + 1;
            if (current_direction == DSignal.Direction.LEFT) { // erase right
                rect_x = coord;
                rect_width = respective_edge - coord;
            }
            else { // erase left
                rect_x = respective_edge;
                rect_width = coord - respective_edge;
            }
        }
        super.d_sig.gc.fillRect(rect_x, rect_y, rect_width, rect_height);
    }
}
