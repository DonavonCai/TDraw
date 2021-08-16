package Controller.Helper;

public class DirectionTrackerFSM {
    protected enum State {STOP, CLICK, FIRST_LEFT, FIRST_RIGHT, DRAG_LEFT, DRAG_RIGHT, FLIP_LEFT, FLIP_RIGHT};
    protected State state;

    protected enum Dir {LEFT, RIGHT, NULL}
    protected Dir direction;
    private boolean directionChange;
    private int prevCoord;

    public DirectionTrackerFSM() {
        state = State.STOP;
        prevCoord = 0;
        direction = Dir.NULL;
    }

    public void Reset() {
        state = State.STOP;
        prevCoord = 0;
        direction = Dir.NULL;
    }

    public boolean ChangedDirection() {
        return directionChange;
    }

    public boolean movingRight() {
        return direction == Dir.RIGHT;
    }

    public boolean movingLeft() {
        return direction == Dir.LEFT;
    }

    public void Tick(int curCoord) {
        // Transitions
        switch(state) {
            case STOP:
                state = State.CLICK;
                break;
            case CLICK:
                if (curCoord < prevCoord) {
                    state = State.FIRST_LEFT;
                }
                else if (prevCoord < curCoord) {
                    state = State.FIRST_RIGHT;
                }
                break;
            case FIRST_LEFT:
            case DRAG_LEFT:
            case FLIP_LEFT:
                if (curCoord < prevCoord) {
                    state = State.DRAG_LEFT;
                }
                else if (prevCoord < curCoord) {
                    state = State.FLIP_RIGHT;
                }
                break;
            case FIRST_RIGHT:
            case DRAG_RIGHT:
            case FLIP_RIGHT:
                if (curCoord < prevCoord) {
                    state = State.FLIP_LEFT;
                }
                else if (prevCoord < curCoord) {
                    state = State.DRAG_RIGHT;
                }
                break;
            default:
                state = State.STOP;
                break;
        }

        // State actions
        switch(state) {
            case FIRST_LEFT:
            case DRAG_LEFT:
                direction = Dir.LEFT;
                directionChange = false;
                break;
            case FIRST_RIGHT:
            case DRAG_RIGHT:
                direction = Dir.RIGHT;
                directionChange = false;
                break;
            case FLIP_LEFT:
                direction = Dir.LEFT;
                directionChange = true;
                break;
            case FLIP_RIGHT:
                direction = Dir.RIGHT;
                directionChange = true;
                break;
            default:
                directionChange = false;
                break;
        }

        // Make sure to update previous coord
        prevCoord = curCoord;
    }
}
