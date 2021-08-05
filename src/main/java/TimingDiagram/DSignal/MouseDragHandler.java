package TimingDiagram.DSignal;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import TimingDiagram.DSignal.Edge.Edge;

class MouseDragHandler extends Handler {

    MouseDragHandler(DSignal d, DirectionTracker t, DragBoundsTracker t2) {
        super(d, t, t2);
    }

    @Override
    public void handle(MouseEvent event) {
        boolean leftClick = event.getButton() == MouseButton.PRIMARY;
        boolean rightClick = event.getButton() == MouseButton.SECONDARY;

        if (!leftClick && !rightClick)
            return;

        directionTracker.setCurCoord((int)event.getX());
        int coord = directionTracker.getCurCoord();

        // Handle initial move
        if (directionTracker.previous_direction == DirectionTracker.Direction.NULL) {
            if (directionTracker.movingLeft())
                initialLeftMove(event);
            else if (directionTracker.movingRight())
                initialRightMove(event);
        }

        // Set current direction
        if (directionTracker.movingLeft())
            directionTracker.current_direction = DirectionTracker.Direction.LEFT;
        else if (directionTracker.movingRight())
            directionTracker.current_direction = DirectionTracker.Direction.RIGHT;

        // Check for direction change and handle
        if (directionTracker.directionChangeDetected()) {
            handleDirectionChange(event);
        }

        dragBoundsTracker.updateBounds(event, leftNeighbor(coord), rightNeighbor(coord));
        dragVerticalLine(coord, leftClick, rightClick);

        clear_covered_edges(event);

        // update previous direction
        directionTracker.previous_direction = directionTracker.current_direction;
        directionTracker.setPrevCoord(coord);
    }

    // Helper functions: ----------------------------------------------------------------------
    // Continuously draw vertical line as mouse as dragged, as long as the line is valid.
    private void dragVerticalLine(int coord, boolean leftClick, boolean rightClick) {
        boolean positiveLeftNeighbor = leftNeighborType(coord) == Edge.Type.POS;
        boolean negativeLeftNeighbor = !positiveLeftNeighbor;
        boolean pastLeftMost = coord <= dragBoundsTracker.getLeftMost();
        boolean pastRightMost = coord >= dragBoundsTracker.getRightMost();

        if (leftClick) {
            // Either no edges erased, or both left and right erased.
            // In any case, edges are balanced, so draw a vertical line if left neighbor is a negative edge.
            if (leftNeighborType(coord) != rightNeighborType(coord)) {
                if (negativeLeftNeighbor && (pastRightMost || pastLeftMost)) {
                    draw_vertical(coord);
                }
            }
            // Only one of the edges has been erased, must find out which one.
            else {
                if (directionTracker.erasedLeft && pastLeftMost) {
                    draw_vertical(coord);
                }
                else if (directionTracker.erasedRight && pastRightMost) {
                    draw_vertical(coord);
                }
            }
        }
        else {
            if (leftNeighborType(coord) != rightNeighborType(coord)) {
                if (positiveLeftNeighbor && (pastRightMost || pastLeftMost)) {
                    draw_vertical(coord);
                }
            }
            else {
                if (directionTracker.erasedLeft && pastLeftMost)
                    draw_vertical(coord);
                else if (directionTracker.erasedRight && pastRightMost)
                    draw_vertical(coord);
            }
        }

        // Extend the signal to where the mouse is dragged.
        if (directionTracker.get_hpos() == DirectionTracker.H_Position.HIGH)
            d_sig.draw_high(d_sig.curEdge.getCoord(), coord, directionTracker.current_direction);
        else
            d_sig.draw_low(d_sig.curEdge.getCoord(), coord, directionTracker.current_direction);
    }

    private void handleDirectionChange(MouseEvent event) {
        int coord = (int)event.getX();

//        dragBoundsTracker.directionChange(coord);
        d_sig.curEdge.setCoord(coord);

        boolean inHigh = (leftNeighborType(coord) == Edge.Type.POS);
        boolean rightToLeft = (directionTracker.current_direction == DirectionTracker.Direction.LEFT);
        boolean edgeErased = directionTracker.erasedLeft || directionTracker.erasedRight;

        if (event.getButton() == MouseButton.PRIMARY && (!inHigh || edgeErased)) {
            d_sig.curEdge.calculateLocation(d_sig.getCanvasWidth());
            if (rightToLeft) {
                d_sig.curEdge.setType(Edge.Type.NEG);
                if (d_sig.QRightEdge == null) {
                    d_sig.QRightEdge = new Edge(Edge.Type.NEG, Edge.Location.MID, coord);
                    System.out.println("right to left: added new QRight");
                }
                else if (d_sig.QRightEdge.getCoord() < coord) {
                    d_sig.QRightEdge.setCoord(coord);
                    d_sig.QLeftEdge.setType(Edge.Type.NEG);
                    System.out.println("right to left: updated QRight to: " + coord);
                }
                else {
                    System.out.println("QRight: " + d_sig.QRightEdge.getCoord() + ", coord: " + coord);
                }
            }
            else { // left to right
                d_sig.curEdge.setType(Edge.Type.POS);
                if (d_sig.QLeftEdge == null) {
                    d_sig.QLeftEdge = new Edge(Edge.Type.POS, Edge.Location.MID, coord);
                    System.out.println("left to right: added new QLeft");
                }
                else if (coord < d_sig.QLeftEdge.getCoord()) {
                    d_sig.QLeftEdge.setCoord(coord);
                    d_sig.QLeftEdge.setType(Edge.Type.POS);
                }
            }
        }
        else if (event.getButton() == MouseButton.SECONDARY && (inHigh || edgeErased)) {
            d_sig.curEdge.calculateLocation(d_sig.getCanvasWidth());
            if (rightToLeft) {
                d_sig.curEdge.setType(Edge.Type.POS);
                if (d_sig.QRightEdge == null)
                    d_sig.QRightEdge = new Edge(Edge.Type.POS, Edge.Location.MID, coord);
                else if (d_sig.QRightEdge.getCoord() < coord) {
                    d_sig.QRightEdge.setCoord(coord);
                    d_sig.QLeftEdge.setType(Edge.Type.POS);
                }
            }
            else {
                d_sig.curEdge.setType(Edge.Type.NEG);
                if (d_sig.QLeftEdge == null)
                    d_sig.QLeftEdge = new Edge(Edge.Type.NEG, Edge.Location.MID, coord);
                else if (coord < d_sig.QLeftEdge.getCoord()) {
                    d_sig.QLeftEdge.setCoord(coord);
                    d_sig.QLeftEdge.setType(Edge.Type.NEG);
                }
            }
        }
    }

    private void initialLeftMove(MouseEvent event) {
        System.out.println("initial left move");
        int coord = (int)event.getX();
        boolean leftClick = event.getButton() == MouseButton.PRIMARY;
        directionTracker.initial_direction = DirectionTracker.Direction.LEFT;

        // Queue up a right edge only if the press creates a new edge.
        // Don't queue up a right edge if the press doesn't create a new edge.
        // left click in high signal => QRight is null
        // right click in low signal => QRight is null
        if ((leftClick && !in_high_signal(coord)) || (!leftClick && in_high_signal(coord))) {
            dragBoundsTracker.setRightMost(coord);
            d_sig.QRightEdge = new Edge();
            d_sig.QRightEdge.setLocation(Edge.Location.MID);
            d_sig.QRightEdge.setCoord(coord);
        }

        // If moving left on a left click, then the press edge is negative.
        if (leftClick) {
            if (!in_high_signal(coord)) {
                d_sig.pressEdge.setType(Edge.Type.NEG);
                d_sig.curEdge.copy(d_sig.pressEdge);
                // Queue up a right edge
                d_sig.QRightEdge.setType(Edge.Type.NEG);
            }
        }
        // Right click
        else {
            if (in_high_signal(coord)) {
                d_sig.pressEdge.setType(Edge.Type.POS);
                d_sig.curEdge.copy(d_sig.pressEdge);
                d_sig.QRightEdge.setType(Edge.Type.POS);
            }
        }
    }

    private void initialRightMove(MouseEvent event) {
        System.out.println("initial right move");
        int coord = (int)event.getX();
        boolean leftClick = event.getButton() == MouseButton.PRIMARY;
        directionTracker.initial_direction = DirectionTracker.Direction.RIGHT;

        // Queue up a left edge only if the press creates a new edge.
        // Don't queue up a left edge if the press doesn't create a new edge.
        // left click in high signal => QLeft is null
        // right click in low signal => QLeft is null
        if ((leftClick && !in_high_signal(coord)) || (!leftClick && in_high_signal(coord))) {
            dragBoundsTracker.setLeftMost(coord);
            d_sig.QLeftEdge = new Edge();
            d_sig.QLeftEdge.setLocation(Edge.Location.MID);
            d_sig.QLeftEdge.setCoord(coord);
        }

        if (leftClick) {
            if (!in_high_signal(coord)) {
                d_sig.pressEdge.setType(Edge.Type.POS);
                d_sig.curEdge.copy(d_sig.pressEdge);
                if (d_sig.QLeftEdge != null)
                    d_sig.QLeftEdge.setType(Edge.Type.POS);
            }
        }
        // Right click
        else {
            if (in_high_signal(coord)) {
                d_sig.pressEdge.setType(Edge.Type.NEG);
                d_sig.curEdge.copy(d_sig.pressEdge);
                if (d_sig.QLeftEdge != null)
                    d_sig.QLeftEdge.setType(Edge.Type.NEG);
            }
        }
    }

    // NOTE: Java's sublist.clear is (inclusive, exclusive)
    // This is accounted for in the helper function clearEdges().
    // Here, make sure left and right are BOTH inclusive.
    private void clear_covered_edges(MouseEvent event) {
        // fixme: erasing the dummy edge
        int left, right;
        boolean checkedLeft = false;
        boolean checkedRight = false;
        int curr = (int)event.getX();

        // Erase on left side of mouse (moving right)
        if (d_sig.curEdge.getCoord() <= curr) {
            // erase everything up to the
            right = last_idx_less_than(curr);
            left = first_idx_greater_than((int)d_sig.curEdge.getCoord());
            checkedRight = true;
        }
        // Erase on right side of mouse (moving left)
        else {
            left = first_idx_greater_than(curr);
            right = last_idx_less_than((int)d_sig.curEdge.getCoord());
            checkedLeft = true;
        }
        // avoid erasing start and end edges
        if (left <= 0)
            left = 1;
        if (right >= d_sig.edges.size() - 1)
            right = d_sig.edges.size() - 2;

        if (checkedLeft && left <= right) {
            System.out.println("erased left");
            directionTracker.erasedLeft = true;
        }
        else if (checkedRight && left <= right){
            System.out.println("erased right");
            directionTracker.erasedRight = true;
        }
        clearEdges(left, right);
    }

    private void clearEdges(int left, int right) {
        if (left != -1 && right != -1 && left <= right) {
            right += 1; // note: subList(fromIndex, toIndex) method is inclusive for fromIndex and exclusive for toIndex
            d_sig.edges.subList(left, right).clear();
        }
    }
}
