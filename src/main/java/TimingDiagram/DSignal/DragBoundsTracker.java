package TimingDiagram.DSignal;

import TimingDiagram.DSignal.Edge.Edge;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

class DragBoundsTracker {
    private int rightMost;
    private int leftMost;

    DragBoundsTracker() {
        leftMost = Integer.MAX_VALUE;
        rightMost = Integer.MIN_VALUE;
    }

    public void reset() {
        leftMost = Integer.MAX_VALUE;
        rightMost = Integer.MIN_VALUE;
    }

    public int getRightMost() {
        return rightMost;
    }

    public int getLeftMost() {
        return leftMost;
    }

    public void setRightMost(int i) {
        rightMost = i;
    }

    public void setLeftMost(int i) {
        leftMost = i;
    }

    public void updateBounds(MouseEvent e, Edge leftNeighbor, Edge rightNeighbor) {
            int coord = (int)e.getX();
            boolean leftClick = e.getButton() == MouseButton.PRIMARY;

            if (leftClick) {
                if (coord > rightMost) {
                    if (rightNeighbor.getType() == Edge.Type.NEG) {
                        System.out.println("update right");
                        rightMost = ((int)rightNeighbor.getCoord());
                    }
                    else {
                        rightMost = coord;
                    }
                }
                else if (coord < leftMost) {
                    if (leftNeighbor.getType() == Edge.Type.POS)
                        leftMost = (int)leftNeighbor.getCoord();
                    else
                        leftMost = coord;
                }
            }
            else {
                if (coord > rightMost) {
                    if (rightNeighbor.getType() == Edge.Type.POS) {
                        System.out.println("update right");
                        rightMost = ((int)rightNeighbor.getCoord());
                    }
                    else {
                        rightMost = coord;
                    }
                }
                else if (coord < leftMost) {
                    if (leftNeighbor.getType() == Edge.Type.NEG)
                        leftMost = (int)leftNeighbor.getCoord();
                    else
                        leftMost = coord;
                }
            }
    }
}
