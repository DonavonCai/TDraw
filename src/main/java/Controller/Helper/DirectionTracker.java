package Controller.Helper;

import java.io.Serializable;

class DirectionTracker implements Serializable {
    protected enum Direction {LEFT, RIGHT, NULL}
    protected enum H_Position{HIGH, LOW}

    private int prevCoord;
    private int curCoord;

    private H_Position h_line_position;
    protected Direction previous_direction;
    protected Direction current_direction;
    protected Direction initial_direction;

    protected boolean erasedLeft;
    protected boolean erasedRight;

    DirectionTracker() {
        reset();
    }

    void reset() {
        prevCoord = -1;
        previous_direction = Direction.NULL;
        current_direction = Direction.NULL;
        initial_direction = Direction.NULL;
        h_line_position = H_Position.LOW;
    }

    // Setters and getters:
    void setCurCoord(int c) {curCoord = c;}

    int getCurCoord() {return curCoord;}

    void setPrevCoord(int c) { prevCoord = c; }

    // Other functions:
    void set_drawhigh() { h_line_position = H_Position.HIGH; }

    void set_drawlow() { h_line_position = H_Position.LOW; }

    H_Position get_hpos() { return h_line_position; }

    void clear_prevDirection() {
        previous_direction = Direction.NULL;
    }

    boolean directionChangeDetected() {
        return current_direction != previous_direction && previous_direction != DirectionTracker.Direction.NULL;
    }

    boolean movingLeft() {
        return prevCoord > 0 && curCoord < prevCoord;
    }

    boolean movingRight() {
        return prevCoord > 0 && curCoord > prevCoord;
    }

}
