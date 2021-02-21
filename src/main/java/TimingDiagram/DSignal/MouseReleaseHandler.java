package TimingDiagram.DSignal;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import TimingDiagram.DSignal.Edge.Edge;

class  MouseReleaseHandler extends Handler {

    MouseReleaseHandler(DSignal d, DirectionTracker t, DragBoundsTracker t2) {
        super(d, t, t2);
    }

    /* On release, decide whether to add 0, 1, or 2 edges from press, drag, release motion */
    @Override
    public void handle(MouseEvent event) {
        final boolean leftClick = event.getButton() == MouseButton.PRIMARY;
        final boolean rightClick = event.getButton() == MouseButton.SECONDARY;
        double coord = event.getX();

        if (!leftClick && !rightClick)
            return;

        d_sig.isDragging = false;
        directionTracker.reset();
        dragBoundsTracker.reset();

        setLeftAndRightEdges(coord);

        System.out.println("left is: " + d_sig.QLeftEdge.getCoord() + ", " + d_sig.QLeftEdge.getType());
        System.out.println("right is: " + d_sig.QRightEdge.getCoord() + ", " + d_sig.QRightEdge.getType());

        addEdges(leftClick, rightClick);

        System.out.println("AFTER RELEASE: ==============================");
        d_sig.printEdges();
        System.out.println("=============================================");
    }

    private void setLeftAndRightEdges(double coord) {
        // prepare QLeftEdge or QRightEdge for adding if null
        if (d_sig.QLeftEdge == null) {
            Edge.Type t;
            if (d_sig.QRightEdge == null)
                d_sig.QRightEdge = new Edge(null, Edge.Location.MID, coord);

            if (d_sig.QRightEdge.getType() != null) {
                t = Edge.oppositeType(d_sig.QRightEdge.getType());
            }
            else {
                t = Edge.oppositeType(rightNeighborType(d_sig.QRightEdge));
                System.out.println("t is " + t);
            }
            d_sig.QLeftEdge = new Edge(t, Edge.Location.MID, coord);
        }
        else if (d_sig.QRightEdge == null) {
            System.out.println("QRight is null");
            Edge.Type t;
            if (d_sig.QLeftEdge == null)
                d_sig.QLeftEdge = new Edge(null, Edge.Location.MID, coord);

            if (d_sig.QLeftEdge.getType() != null) {
                t = Edge.oppositeType(d_sig.QLeftEdge.getType());
            }
            else {
                t = Edge.oppositeType(leftNeighborType(d_sig.QLeftEdge));
            }
            d_sig.QRightEdge = new Edge(t, Edge.Location.MID, coord);
        }
    }

    private void addEdges(boolean leftClick, boolean rightClick) {
        // Add the left edge:
        /* For a left click:
         * Only add QLeftEdge if there is a negative edge to its left.
         * Otherwise, if QLeftEdge is positive, then there are 2 positive edges in a row (illegal)...
         * And if QLeftEdge is negative, then the left click will be lowering the signal (not what user wants) */
        if (leftClick) {
            if (d_sig.QLeftEdge.getType() != leftNeighborType(d_sig.QLeftEdge) && leftNeighborType(d_sig.QLeftEdge) == Edge.Type.NEG)
                d_sig.addEdgeAndSort(d_sig.QLeftEdge);
        }
        // And vice versa for a right click:
        else if (rightClick) {
            if (d_sig.QLeftEdge.getType() != leftNeighborType(d_sig.QLeftEdge) && leftNeighborType(d_sig.QLeftEdge) == Edge.Type.POS)
                d_sig.addEdgeAndSort(d_sig.QLeftEdge);
        }

        // Now add the right edge:
        if (d_sig.QRightEdge.getType() != rightNeighborType(d_sig.QRightEdge)) {
            if (d_sig.QRightEdge.getType() == Edge.Type.POS && leftNeighborType(d_sig.QRightEdge) == rightNeighborType(d_sig.QRightEdge))
                d_sig.addEdgeAndSort(d_sig.QRightEdge);

            else if (d_sig.QRightEdge.getType() == Edge.Type.NEG && leftNeighborType(d_sig.QRightEdge) == rightNeighborType(d_sig.QRightEdge))
                d_sig.addEdgeAndSort(d_sig.QRightEdge);
        }
    }

}
