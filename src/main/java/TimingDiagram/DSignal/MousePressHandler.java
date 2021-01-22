package TimingDiagram.DSignal;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import TimingDiagram.DSignal.Edge.Edge;

class MousePressHandler extends Handler {

    MousePressHandler(DSignal d, DirectionTracker t) {
        super(d, t);
    }

    @Override
    public void handle(MouseEvent event) {
        double coord = event.getX();
        if (event.getButton() == MouseButton.PRIMARY) {
            // Initially assume that the press edge is positive
            // If the user drags left, then correct in drag handler.
            d_sig.pressEdge = new Edge(Edge.Type.POS, Edge.Location.MID, coord);
//            d_sig.edgeToAdd = new Edge(d_sig.pressEdge);
            d_sig.QLeftEdge = null;
            d_sig.QRightEdge = null;
            directionTracker.clear_prevDirection();
            directionTracker.set_drawhigh();
            d_sig.pressEdge.setCoord(coord);
            d_sig.pressEdge.setLocation(Edge.Location.MID); // clicks are only registered in the middle of canvas
            d_sig.curEdge = new Edge(d_sig.pressEdge);
            d_sig.isDragging = true;
            if (!in_high_signal(coord))
                draw_vertical(coord);
        }
        else if (event.getButton() == MouseButton.SECONDARY) {
            d_sig.pressEdge = new Edge(Edge.Type.NEG, Edge.Location.MID, coord);
//            d_sig.edgeToAdd = new Edge(d_sig.pressEdge);
            d_sig.QLeftEdge = null;
            d_sig.QRightEdge = null;
            directionTracker.clear_prevDirection();
            directionTracker.set_drawlow();
            d_sig.pressEdge.setCoord(coord);
            d_sig.pressEdge.setType(Edge.Type.NEG);
            d_sig.pressEdge.setLocation(Edge.Location.MID);
            d_sig.curEdge = new Edge(d_sig.pressEdge);
            d_sig.isDragging = true;
            if (in_high_signal(coord))
                draw_vertical(coord);
        }
    }
}
