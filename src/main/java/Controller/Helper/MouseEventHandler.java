package Controller.Helper;

import Model.Signal;
import View.SignalView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class MouseEventHandler {
    private int leftBound;
    private int rightBound;

    private boolean firstTick = true;

    DirectionTrackerFSM directionTracker;

    public MouseEventHandler() {
        directionTracker = new DirectionTrackerFSM();
    }

    public void SetBounds(int left, int right) {
        leftBound = left;
        rightBound = right;
    }

    public void CreateEventHandlers(Signal signalModel, SignalView signalView) {
        Pane signalPane = signalView.GetSignalPane();

        signalModel.Initialize(leftBound, rightBound);

        signalPane.addEventHandler(MouseEvent.MOUSE_PRESSED,
            new javafx.event.EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    firstTick = true;
                    directionTracker.Reset();

                    Anchor(signalModel, event);
                }
            }
        );
        signalPane.addEventHandler(MouseEvent.MOUSE_DRAGGED,
            new javafx.event.EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    int coord = (int)event.getX();

                    directionTracker.Tick(coord);

                    if (directionTracker.ChangedDirection()) {
                        Anchor(signalModel, event);
                    }

                    if (directionTracker.movingRight()) {
                        signalModel.Extend(coord);
                    }
                    else if (directionTracker.movingLeft()) {
                        if (firstTick) {
                            signalModel.SetDirectionLeft();
                            firstTick = false;
                        }
                        signalModel.Extend(coord);
                    }
                }
            }
        );
        signalPane.addEventHandler(MouseEvent.MOUSE_RELEASED,
            new javafx.event.EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    signalModel.PrintEdges();
                }
            }
        );
    }

    private void Anchor(Signal signalModel, MouseEvent event) {
        int coord = (int)event.getX();
        if (LeftClick(event)) {
            signalModel.Anchor(Signal.Type.HIGH, coord);
        }
        else if (RightClick(event)) {
            signalModel.Anchor(Signal.Type.LOW, coord);
        }
    }

    private boolean LeftClick(MouseEvent event) {
        return event.getButton() == MouseButton.PRIMARY;
    }

    private boolean RightClick(MouseEvent event) {
        return event.getButton() == MouseButton.SECONDARY;
    }
}
