package TimingDiagram.DSignal;

import TimingDiagram.DSignal.Edge.Edge;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

class DragBoundsTracker {
    private int rightMost;
    private int leftMost;
    private DSignal d_signal;

    DragBoundsTracker(DSignal sig) {
        leftMost = Integer.MAX_VALUE;
        rightMost = Integer.MIN_VALUE;
        d_signal = sig;
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

//    public void directionChange(int coord) {
//        System.out.println("before leftMost: " + leftMost);
//        System.out.println("before rightMost: " + rightMost);
//        if (coord < leftMost) {
//            leftMost = coord;
//            System.out.println("leftMost: " + leftMost);
//        }
//        else if (rightMost < coord) {
//            rightMost = coord;
//            System.out.println("rightMost: " + rightMost);
//        }
//    }


    // Note: leftNeighbor and rightNeighbor are the edges surrounding the coordinate of e.
    public void updateBounds(MouseEvent e, Edge leftNeighbor, Edge rightNeighbor) {
            int coord = (int)e.getX();
            boolean leftClick = e.getButton() == MouseButton.PRIMARY;

            if (leftClick) {
                if (coord > rightMost) {
                    // middle of original high signal
                    if (rightNeighbor.getType() == Edge.Type.NEG) {
                        System.out.println("update right, left click");
                        rightMost = ((int)rightNeighbor.getCoord());
                    }
                    // to the right of original high signal
                    else {
                        rightMost = coord;
                    }
                }
                else if (coord < leftMost) {
                    if (leftNeighbor.getType() == Edge.Type.POS) {
                        System.out.println("update left, left click");
                        leftMost = (int) leftNeighbor.getCoord();
                    }
                    else {
                        leftMost = coord;
                    }
                }
            }
            else {
                if (coord > rightMost) {
                    if (rightNeighbor.getType() == Edge.Type.POS) {
                        System.out.println("update right, right click");
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
