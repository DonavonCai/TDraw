package TimingDiagram.DSignal;

import java.io.Serializable;

class DirectionTracker implements Serializable {
    protected enum Direction {LEFT, RIGHT, NULL}
    protected enum H_Position{HIGH, LOW}

    protected int prev_mouse_coord;
    private H_Position h_line_position;
    protected Direction previous_direction;
    protected Direction current_direction;
    protected Direction initial_direction;

    DirectionTracker() {
        reset();
    }

    void reset() {
        prev_mouse_coord = -1;
        previous_direction = Direction.NULL;
        current_direction = Direction.NULL;
        initial_direction = Direction.NULL;
        h_line_position = H_Position.LOW;
    }

    void set_drawhigh() { h_line_position = H_Position.HIGH; }
    void set_drawlow() { h_line_position = H_Position.LOW; }
    H_Position get_hpos() { return h_line_position; }

    void clear_prevDirection() {previous_direction = Direction.NULL;}
}
