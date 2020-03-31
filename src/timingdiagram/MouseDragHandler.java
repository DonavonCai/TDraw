package timingdiagram;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

public class MouseDragHandler extends Handler {

    MouseDragHandler(DSignal d) {
        super(d);
    }

    @Override
    public void handle(MouseEvent event) {
        // get mouse direction
        if (super.d_sig.prev_mouse_coord > 0 && (int)event.getX() < super.d_sig.prev_mouse_coord) { // moving left
            set_moving_left(event);
        }
        else if (super.d_sig.prev_mouse_coord > 0 && (int)event.getX() > super.d_sig.prev_mouse_coord) { // moving right
            set_moving_right(event);
        }

        if (super.d_sig.current_direction != super.d_sig.previous_direction && super.d_sig.previous_direction != DSignal.Direction.NULL) { // direction change
//            System.out.println("direction change at: " + (int)event.getX());
            super.d_sig.current_edge = (int)event.getX();

            if (!in_between_edges(super.d_sig.current_edge)) {
//                System.out.println("dir change: adding edge at: " + super.d_sig.current_edge);
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (d_sig.previous_direction == DSignal.Direction.RIGHT) { // right to left
                        System.out.println("neg edge");
                        super.d_sig.neg_edges.add(d_sig.current_edge);
                        Collections.sort(super.d_sig.neg_edges);
                        System.out.println("num neg: " + super.d_sig.neg_edges.size());
                    }
                    else if (d_sig.previous_direction == DSignal.Direction.LEFT){ // left to right
                        System.out.println("dir change: pos edge");
                        super.d_sig.pos_edges.add(d_sig.current_edge);
                        Collections.sort(super.d_sig.pos_edges);
                    }
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    if (d_sig.previous_direction == DSignal.Direction.RIGHT) { // right to left
                        System.out.println("pos edge");
                        super.d_sig.pos_edges.add(d_sig.current_edge);
                        Collections.sort(super.d_sig.pos_edges);
                    }
                    else { // left to right
                        System.out.println("neg edge");
                        super.d_sig.neg_edges.add(d_sig.current_edge);
                        Collections.sort(super.d_sig.neg_edges);
                    }
                }
            }
        }

        if (!super.in_between_edges((int)event.getX())) { // this vertical line is drawn over in draw_horizontal if mouse continues to be dragged
            super.draw_vertical((int)event.getX());
        }
        draw_horizontal((int) event.getX(), super.d_sig.current_edge, super.d_sig.current_direction);

        clear_covered_edges(event);

        // update direction
        super.d_sig.previous_direction = super.d_sig.current_direction;
        super.d_sig.prev_mouse_coord = (int)event.getX();
    }

    // Helper functions: ----------------------------------------------------------------------
    private void set_moving_left(MouseEvent event) {
        if (super.d_sig.initial_direction == DSignal.Direction.NULL) { // set initial direction
//            System.out.println("initial");
            super.d_sig.initial_direction = DSignal.Direction.LEFT;
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!in_between_edges(super.d_sig.initial_edge)) {
                    // change initial from positive to negative (if moving left, press handler will have assumed a positive edge placement, but it should be negative, and is corrected here)
                    System.out.println("switching pos and neg");
                    super.d_sig.neg_edges.add(super.d_sig.initial_edge);
                    super.d_sig.pos_edges.remove(Integer.valueOf(super.d_sig.initial_edge)); // valueOf returns an Integer object so that we remove by value instead of index
                    Collections.sort(super.d_sig.neg_edges);
                    super.d_sig.release_edge_pos = true;
                }
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                if (!in_between_edges(super.d_sig.initial_edge)) {
                    // change initial from negative to positive
                    System.out.println("switching neg and neg");
                    super.d_sig.pos_edges.add(super.d_sig.initial_edge);
                    super.d_sig.neg_edges.remove(Integer.valueOf(super.d_sig.initial_edge)); // valueOf returns an Integer object so that we remove by value instead of index
                    Collections.sort(super.d_sig.pos_edges);
                    super.d_sig.release_edge_pos = false;
                }
            }
        }
        super.d_sig.current_direction = DSignal.Direction.LEFT;
    }

    private void set_moving_right(MouseEvent event) {
        if (super.d_sig.initial_direction == DSignal.Direction.NULL) { // set initial direction
            super.d_sig.initial_direction = DSignal.Direction.RIGHT;
        }
        if (event.getButton() == MouseButton.PRIMARY) {
            super.d_sig.release_edge_pos = false;
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            super.d_sig.release_edge_pos = true;
        }
        super.d_sig.current_direction = DSignal.Direction.RIGHT;
    }

    private void clear_covered_edges(MouseEvent event) {
//        System.out.println("clearing");
        int left_pos = -1;
        int right_pos = -1;
        int left_neg = -1;
        int right_neg = -1;
        int curr = (int)event.getX();

        if (super.d_sig.current_edge <= curr) { // erasing on the right side
//            System.out.println("erasing right");
            if (event.getButton() == MouseButton.PRIMARY) {
                super.d_sig.release_edge_pos = false;
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                super.d_sig.release_edge_pos = true;
            }
            // get range of pos edges to delete
            left_pos = first_idx_greater_than(super.d_sig.current_edge, 'p');
            right_pos = last_idx_less_than(curr, 'p');

            // range of neg edges to delete
            left_neg = first_idx_greater_than(super.d_sig.current_edge, 'n');
            right_neg = last_idx_less_than(curr, 'n');

        }
        else { // erasing on the left side
//            System.out.println("erasing left");
            if (event.getButton() == MouseButton.PRIMARY) {
                super.d_sig.release_edge_pos = true;
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                System.out.println("release negative");
                super.d_sig.release_edge_pos = false;
            }
            left_pos = first_idx_greater_than(curr, 'p');
            right_pos = last_idx_less_than(super.d_sig.current_edge, 'p');

            left_neg = first_idx_greater_than(curr, 'n');
            right_neg = last_idx_less_than(super.d_sig.current_edge, 'n');
        }
        clear_pos(left_pos, right_pos);
        clear_neg(left_neg, right_neg);
    }

    private void clear_pos(int left, int right) {
        if (left != -1 && right != -1 && left <= right) {
            System.out.println("removing pos from i: " + left + ": " + super.d_sig.pos_edges.get(left) + " to " + right + ": " + super.d_sig.pos_edges.get(right));
            right += 1; // note: subList(fromIndex, toIndex) method is inclusive for fromIndex and exclusive for toIndex
            super.d_sig.pos_edges.subList(left, right).clear();
        }
    }

    private void clear_neg(int left, int right) {
        if (left != -1 && right != -1 && left <= right) {
            System.out.println("removing neg from i: " + left + ": " + super.d_sig.neg_edges.get(left) + " to " + right + ": " + super.d_sig.neg_edges.get(right));
            right += 1;
            super.d_sig.neg_edges.subList(left, right).clear();
            System.out.println("size is now: " + super.d_sig.neg_edges.size());
        }
    }

    private int first_idx_greater_than(int edge, char code) { // first index greater than edge
        int r = -1;
        ArrayList<Integer> arr = null;
        if (code == 'p') { // positive
            arr = super.d_sig.pos_edges;
        }
        else if (code == 'n') { // negative
            arr = super.d_sig.neg_edges;
        }
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) > edge) {
                return i;
            }
        }
        return r;
    }

    private int last_idx_less_than(int edge, char code) { // first index less than edge
        int r = -1;
        ArrayList<Integer> arr = null;
        if (code == 'p') {
            arr = super.d_sig.pos_edges;
        }
        else if (code == 'n') {
            arr = super.d_sig.neg_edges;
        }

        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) < edge) {
                r = i;
            }
        }
        return r;
    }

    protected void draw_horizontal(int coord, int respective_edge, DSignal.Direction current_direction) {
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
