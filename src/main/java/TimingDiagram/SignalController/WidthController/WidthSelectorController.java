package TimingDiagram.SignalController.WidthController;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.paint.Color;

import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import javafx.beans.property.BooleanProperty;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import TimingDiagram.SignalController.SignalController;

public class WidthSelectorController {
    SignalController parent;
    @FXML
    VBox widthSelector;
    @FXML
    Polygon arrowHead;
    @FXML
    Line line;

    static class DragDelta {double xOld, xNew;}
    final DragDelta dragDelta = new DragDelta();

    private final long MIN_STATIONARY_TIME = 100_000_000 ; // nanoseconds
    private BooleanProperty mouseMoving;

    public void setParentAndBindHeight(SignalController p) {
        parent = p;
        line.endYProperty().bind(p.get_signal_box().heightProperty());
    }

    public void initialize() {
        widthSelector.setViewOrder(0);
        arrowHead.getPoints().addAll(
                0.0, 0.0,
                6.0, 0.0,
                3.0, 3.0);
        line.setStartX(0);
        line.setStartY(0);
        line.setEndX(0);
        line.setEndY(50);

        setSolid();
        addEventHandlers();
        trackMouseStopped();
    }

    public void setSolid() {
        arrowHead.setStroke(Color.BLACK);
        line.setStroke(Color.BLACK);
    }

    public void setPreview() {
        arrowHead.setStroke(Color.GRAY);
        line.setStroke(Color.GRAY);
    }

    private void trackMouseStopped() {
        // Code for tracking whether the mouse has stopped dragging. Preview is updated on mouse stop.
        mouseMoving = new SimpleBooleanProperty();
        mouseMoving.addListener((obs, wasMoving, isNowMoving) -> {
            if (! isNowMoving) {
                widthSelector.setLayoutX(dragDelta.xNew);
                dragDelta.xOld = widthSelector.getLayoutX(); // fixme: offset by a little bit
                System.out.println("Stopped at: " + widthSelector.getLayoutX());
            }
        });
        PauseTransition pause = new PauseTransition(Duration.millis(MIN_STATIONARY_TIME / 1_000_000));
        pause.setOnFinished(e -> mouseMoving.set(false));
        widthSelector.setOnMouseDragged(e -> {
            mouseMoving.set(true);
            pause.playFromStart();
        });
    }

    private void addEventHandlers() {
        widthSelector.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        widthSelector.setManaged(false);
                        setPreview();
                        dragDelta.xOld = widthSelector.getLayoutX() - event.getX();
                        System.out.println("xOld is: " + dragDelta.xOld);
                    }
                }
        );
        widthSelector.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        dragDelta.xNew =  dragDelta.xOld + event.getX();
                        // todo: xNew not correct on direction change. Create a directionTracker class from DSignal.DragHandler?
                        System.out.println(dragDelta.xNew);
                    }
                }
        );

        widthSelector.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        widthSelector.setManaged(true);
                        setSolid();
                        // fixme: avoid using magic number. I think this has to do with button_box width? Maybe padding too.
                        double newWidth = widthSelector.getLayoutX() - parent.NAME_WIDTH - 56;
                        parent.setSignalWidths(newWidth);
                    }
                }
        );
    }
}
