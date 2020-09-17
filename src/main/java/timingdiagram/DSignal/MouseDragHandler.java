package timingdiagram.DSignal;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;

class MouseDragHandler extends Handler {

    MouseDragHandler(DSignal d, DirectionTracker t) {
        super(d, t);
    }

    @Override
    public void handle(MouseEvent event) {
        int coord = (int)event.getX();
        // get mouse direction
        if (super.directionTracker.prev_mouse_coord > 0 && coord < super.directionTracker.prev_mouse_coord) { // moving left
            set_moving_left(event);
        }
        else if (super.directionTracker.prev_mouse_coord > 0 && coord > super.directionTracker.prev_mouse_coord) { // moving right
            set_moving_right(event);
        }

        if (super.directionTracker.current_direction != super.directionTracker.previous_direction && super.directionTracker.previous_direction != DirectionTracker.Direction.NULL) { // direction change
            super.d_sig.current_edge = coord;

            if (!in_high_signal(coord) && !in_low_signal(coord)) {
                if (event.getButton() == MouseButton.PRIMARY) {
                    if (directionTracker.previous_direction == DirectionTracker.Direction.RIGHT) { // right to left
                        super.d_sig.neg_edges.add(d_sig.current_edge);
                        Collections.sort(super.d_sig.neg_edges);
                    }
                    else if (directionTracker.previous_direction == DirectionTracker.Direction.LEFT){ // left to right
                        super.d_sig.pos_edges.add(d_sig.current_edge);
                        Collections.sort(super.d_sig.pos_edges);
                    }
                }
                else if (event.getButton() == MouseButton.SECONDARY) {
                    if (directionTracker.previous_direction == DirectionTracker.Direction.RIGHT) { // right to left
                        super.d_sig.pos_edges.add(d_sig.current_edge);
                        Collections.sort(super.d_sig.pos_edges);
                    }
                    else { // left to right
                        super.d_sig.neg_edges.add(d_sig.current_edge);
                        Collections.sort(super.d_sig.neg_edges);
                    }
                }
            }
        }

        if (!super.in_high_signal(coord) && !super.in_low_signal(coord)) { // vertical line is drawn over as mouse is dragged
            super.draw_vertical(coord);
        }
        draw_horizontal(coord, super.d_sig.current_edge, super.directionTracker.current_direction);

        clear_covered_edges(event);

        // update direction
        super.directionTracker.previous_direction = super.directionTracker.current_direction;
        super.directionTracker.prev_mouse_coord = coord;
    }

    // Helper functions: ----------------------------------------------------------------------
    private void set_moving_left(MouseEvent event) {
        int coord = (int)event.getX();
        if (super.directionTracker.initial_direction == DirectionTracker.Direction.NULL) { // set initial direction
            super.directionTracker.initial_direction = DirectionTracker.Direction.LEFT;
            if (event.getButton() == MouseButton.PRIMARY) {
                if (!in_high_signal(coord) && !in_low_signal(coord)) {
                    // change initial from positive to negative (if moving left, press handler will have assumed a positive edge placement, but it should be negative, and is corrected here)
                    super.d_sig.neg_edges.add(super.d_sig.initial_edge);
                    super.d_sig.pos_edges.remove(Integer.valueOf(super.d_sig.initial_edge)); // valueOf returns an Integer object so that we remove by value instead of index
                    Collections.sort(super.d_sig.neg_edges);
                    super.d_sig.release_edge_pos = true;
                }
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                if (!in_high_signal(coord) && !in_low_signal(coord)) {
                    // change initial from negative to positive
                    super.d_sig.pos_edges.add(super.d_sig.initial_edge);
                    super.d_sig.neg_edges.remove(Integer.valueOf(super.d_sig.initial_edge)); // valueOf returns an Integer object so that we remove by value instead of index
                    Collections.sort(super.d_sig.pos_edges);
                    super.d_sig.release_edge_pos = false;
                }
            }
        }
        super.directionTracker.current_direction = DirectionTracker.Direction.LEFT;
    }

    private void set_moving_right(MouseEvent event) {
        if (super.directionTracker.initial_direction == DirectionTracker.Direction.NULL) { // set initial direction
            super.directionTracker.initial_direction = DirectionTracker.Direction.RIGHT;
        }
        if (event.getButton() == MouseButton.PRIMARY) {
            super.d_sig.release_edge_pos = false;
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            super.d_sig.release_edge_pos = true;
        }
        super.directionTracker.current_direction = DirectionTracker.Direction.RIGHT;
    }

    private void clear_covered_edges(MouseEvent event) {
        int left_pos;
        int right_pos;
        int left_neg;
        int right_neg;
        int curr = (int)event.getX();

        if (super.d_sig.current_edge <= curr) { // erasing on the right side
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
            if (event.getButton() == MouseButton.PRIMARY) {
                super.d_sig.release_edge_pos = true;
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
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
            right += 1; // note: subList(fromIndex, toIndex) method is inclusive for fromIndex and exclusive for toIndex
            super.d_sig.pos_edges.subList(left, right).clear();
        }
    }

    private void clear_neg(int left, int right) {
        if (left != -1 && right != -1 && left <= right) {
            right += 1;
            super.d_sig.neg_edges.subList(left, right).clear();
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
        assert arr != null : "fatal error in mouse drag handler (last idx greater than)";
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

        assert arr != null : "fatal error in mouse drag handler (last idx less than)";
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) < edge) {
                r = i;
            }
        }
        return r;
    }

    protected void draw_horizontal(int coord, int respective_edge, DirectionTracker.Direction current_direction) {
        boolean draw_high = (super.directionTracker.get_hpos() == DirectionTracker.H_Position.HIGH);

        super.d_sig.gc.beginPath();
        super.d_sig.gc.setStroke(Color.BLACK);
        super.d_sig.gc.setLineWidth(super.d_sig.line_width);

        // for erasing signal
        int rect_x;
        int rect_y;
        int rect_width;
        int rect_height;

        // erase right
        // erase left
        if (draw_high) {
            super.d_sig.gc.moveTo(respective_edge, 0);
            super.d_sig.gc.lineTo(coord, 0);
            super.d_sig.gc.stroke();

            // erase lower
            super.d_sig.gc.setFill(Color.WHITE);
            rect_y = super.d_sig.line_width - 1;
            rect_height = super.d_sig.height;
        }
        else {
            super.d_sig.gc.moveTo(respective_edge, super.d_sig.height);
            super.d_sig.gc.lineTo(coord, super.d_sig.height);
            super.d_sig.gc.stroke();

            // erase upper
            super.d_sig.gc.setFill(Color.WHITE);
            rect_y = 0;
            rect_height = super.d_sig.height - super.d_sig.line_width + 1;
        }
        if (current_direction == DirectionTracker.Direction.LEFT) { // erase right
            rect_x = coord;
            rect_width = respective_edge - coord;
        }
        else { // erase left
            rect_x = respective_edge;
            rect_width = coord - respective_edge;
        }
        super.d_sig.gc.fillRect(rect_x, rect_y, rect_width, rect_height);
    }
}
