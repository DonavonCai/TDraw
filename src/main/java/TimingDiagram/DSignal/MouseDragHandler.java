package TimingDiagram.DSignal;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import TimingDiagram.DSignal.Edge.Edge;

class MouseDragHandler extends Handler {

    MouseDragHandler(DSignal d, DirectionTracker t) {
        super(d, t);
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

        // Continuously draw vertical line as mouse as dragged, as long as the line is valid.
        // FIXME: draws vertical line on direction change in the middle of signal
        if (leftClick) {
            // If moving left, then the mouse is dragging a positive edge
            if (directionTracker.movingLeft() && leftNeighborType(coord) == Edge.Type.NEG)
                draw_vertical(coord);

            // If moving right, then mouse is dragging a negative edge
            else if (directionTracker.movingRight() && rightNeighborType(coord) == Edge.Type.POS)
                draw_vertical(coord);
        }
        else {
            if (directionTracker.movingLeft() && leftNeighborType(coord) == Edge.Type.POS)
                draw_vertical(coord);
            else if (directionTracker.movingRight() && rightNeighborType(coord) == Edge.Type.NEG)
                draw_vertical(coord);
        }

        // Extend the signal to where the mouse is dragged.
        if (directionTracker.get_hpos() == DirectionTracker.H_Position.HIGH)
            d_sig.draw_high(d_sig.curEdge.getCoord(), coord, directionTracker.current_direction);
        else
            d_sig.draw_low(d_sig.curEdge.getCoord(), coord, directionTracker.current_direction);

        clear_covered_edges(event);

        // update previous direction
        directionTracker.previous_direction = directionTracker.current_direction;
        directionTracker.prevCoord = coord;
    }

    // Helper functions: ----------------------------------------------------------------------
    // FIXME: don't overwrite left/right edge if in middle of signal
    private void handleDirectionChange(MouseEvent event) {
        int coord = (int)event.getX();
        d_sig.curEdge.setCoord(coord);

//        boolean inHigh = in_high_signal(coord);
        boolean inHigh = (leftNeighborType(coord) == Edge.Type.POS);
        boolean rightToLeft = (directionTracker.previous_direction == DirectionTracker.Direction.RIGHT);

        if (event.getButton() == MouseButton.PRIMARY && !inHigh) {
            d_sig.curEdge.calculateLocation(d_sig.getCanvasWidth());
            if (rightToLeft) {
                d_sig.curEdge.setType(Edge.Type.NEG);
//                d_sig.edgeToAdd.copy(d_sig.curEdge);
                if (d_sig.QRightEdge == null)
                    d_sig.QRightEdge = new Edge(Edge.Type.NEG, Edge.Location.MID, coord);
                else if (d_sig.QRightEdge.getCoord() < coord)
                    d_sig.QRightEdge.setCoord(coord);
            }
            // left to right
            else {
                d_sig.curEdge.setType(Edge.Type.POS);
//                d_sig.edgeToAdd.copy(d_sig.curEdge);
                if (d_sig.QLeftEdge == null)
                    d_sig.QLeftEdge = new Edge(Edge.Type.POS, Edge.Location.MID, coord);
                else if (d_sig.QLeftEdge.getCoord() > coord)
                    d_sig.QLeftEdge.setCoord(coord);
            }
        }
        else if (event.getButton() == MouseButton.SECONDARY && inHigh) {
            d_sig.curEdge.calculateLocation(d_sig.getCanvasWidth());
            if (rightToLeft) {
                d_sig.curEdge.setType(Edge.Type.POS);
//                d_sig.edgeToAdd.copy(d_sig.curEdge);
                if (d_sig.QRightEdge == null)
                    d_sig.QRightEdge = new Edge(Edge.Type.POS, Edge.Location.MID, coord);
                else if (d_sig.QRightEdge.getCoord() < coord)
                    d_sig.QRightEdge.setCoord(coord);
            }
            else {
                d_sig.curEdge.setType(Edge.Type.NEG);
//                d_sig.edgeToAdd.copy((d_sig.curEdge));
                if (d_sig.QLeftEdge == null)
                    d_sig.QLeftEdge = new Edge(Edge.Type.NEG, Edge.Location.MID, coord);
                else if (d_sig.QLeftEdge.getCoord() > coord)
                    d_sig.QLeftEdge.setCoord(coord);
            }
        }
    }

    private void initialLeftMove(MouseEvent event) {
        System.out.println("initial left move");
        int coord = (int)event.getX();
        directionTracker.initial_direction = DirectionTracker.Direction.LEFT;

        d_sig.QRightEdge = new Edge();
        d_sig.QRightEdge.setLocation(Edge.Location.MID);
        d_sig.QRightEdge.setCoord(coord);

        // If moving left on a left click, then the press edge is negative.
        if (event.getButton() == MouseButton.PRIMARY) {
            // fixme: drag left in high signal, condition wrong?
            if (!in_high_signal(coord)) {
                d_sig.pressEdge.setType(Edge.Type.NEG);
                d_sig.curEdge.copy(d_sig.pressEdge);

                // We now have a right edge
//                d_sig.edgeToAdd.copy(d_sig.pressEdge);
                d_sig.QRightEdge.setType(Edge.Type.NEG);
//                System.out.println("EDGE TO ADD IS: " + d_sig.edgeToAdd.getType());
            }
        }
        // If moving left on right click, press edge is positive.
        else if (event.getButton() == MouseButton.SECONDARY) {
            if (in_high_signal(coord)) {
                d_sig.pressEdge.setType(Edge.Type.POS);
                d_sig.curEdge.copy(d_sig.pressEdge);

//                d_sig.edgeToAdd.copy(d_sig.pressEdge);
                d_sig.QRightEdge.setType(Edge.Type.POS);
            }
        }
    }

    private void initialRightMove(MouseEvent event) {
        int coord = (int)event.getX();
        directionTracker.initial_direction = DirectionTracker.Direction.RIGHT;

        d_sig.QLeftEdge = new Edge();
        d_sig.QLeftEdge.setLocation(Edge.Location.MID);
        d_sig.QLeftEdge.setCoord(coord);

        // If moving right on a left click, then the press edge is positive.
        if (event.getButton() == MouseButton.PRIMARY) {
            if (!in_high_signal(coord)) {
                d_sig.pressEdge.setType(Edge.Type.POS);
                d_sig.curEdge.copy(d_sig.pressEdge);

                // We now have a right edge
//                d_sig.edgeToAdd.copy(d_sig.pressEdge);
                d_sig.QLeftEdge.setType(Edge.Type.POS);
            }
        }
        // If moving right on right click, press edge is negative.
        else if (event.getButton() == MouseButton.SECONDARY) {
            if (in_high_signal(coord)) {
                d_sig.pressEdge.setType(Edge.Type.NEG);
                d_sig.curEdge.copy(d_sig.pressEdge);

//                d_sig.edgeToAdd.copy(d_sig.pressEdge);
                d_sig.QLeftEdge.setType(Edge.Type.NEG);
            }
        }
    }

    private void clear_covered_edges(MouseEvent event) {
        // fixme: erasing the dummy edge
        int left;
        int right;
        int curr = (int)event.getX();

        // Erase on left side of mouse
        if (d_sig.curEdge.getCoord() <= curr) {
            // get range of edges to delete
            left = first_idx_greater_than((int)d_sig.curEdge.getCoord());
            right = last_idx_less_than(curr);
        }
        // Erase on right side of mouse
        else {
            left = first_idx_greater_than(curr);
            right = last_idx_less_than((int)d_sig.curEdge.getCoord());
        }
        // avoid erasing start and end edges
        if (left <= 0)
            left = 1;
        // note: sublist() is (inclusive, exclusive)
        if (right >= d_sig.edges.size() - 1)
            right = d_sig.edges.size() - 2;

        clearEdges(left, right);
    }

    private void clearEdges(int left, int right) {
        if (left != -1 && right != -1 && left <= right) {
            right += 1; // note: subList(fromIndex, toIndex) method is inclusive for fromIndex and exclusive for toIndex
            d_sig.edges.subList(left, right).clear();
        }
    }
}
