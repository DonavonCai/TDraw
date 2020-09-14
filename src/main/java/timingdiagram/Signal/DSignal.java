package timingdiagram.Signal;

import javafx.geometry.Pos;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import java.io.Serializable;
import java.util.ArrayList;

public class DSignal implements Serializable {
    // layout
    protected final int height;
    protected final int canvas_width;
    protected final int line_width;

    transient private Canvas signal;
    transient protected GraphicsContext gc;

    // direction checking
    private final DirectionTracker directionTracker;
    // event handling
    protected boolean release_edge_pos;
    private final MousePressHandler press_handler;
    private final MouseDragHandler drag_handler;
    private final MouseReleaseHandler release_handler;
    // edge tracking
    protected int current_edge;
    protected int initial_edge;
    protected final ArrayList<Integer> pos_edges;
    protected final ArrayList<Integer> neg_edges;

    public DSignal() {
        // layout
        height = 30;
        canvas_width = 700;
        line_width = 3;
        signal = new Canvas(canvas_width, height);
        gc = signal.getGraphicsContext2D();
        // direction checking
        directionTracker = new DirectionTracker();
        // event handling
        release_edge_pos = false;
        press_handler = new MousePressHandler(this, directionTracker);
        drag_handler = new MouseDragHandler(this, directionTracker);
        release_handler = new MouseReleaseHandler(this, directionTracker);
        // data
        pos_edges = new ArrayList<>();
        neg_edges = new ArrayList<>();
    }

    public HBox draw() { // initializes all elements required for DSignal, including buttons, canvas, event handlers, etc.
        TextField name = new TextField("Signal_Name");
        name.setStyle("-fx-background-color: white;");
        name.setAlignment(Pos.BOTTOM_RIGHT);
        name.setMaxWidth(100.0);

        // style the pane instead of the canvas
        Pane signalPane = new Pane(signal);
        signalPane.setPrefSize(canvas_width, height);
        init_line();
        HBox diagram = new HBox(name, signalPane);
        diagram.setSpacing(5);
        diagram.setAlignment(Pos.BOTTOM_CENTER);

        signalPane.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) { // code is repeated to avoid event handling with mouse buttons other than left and right click
                        press_handler.handle(event);
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        drag_handler.handle(event);
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        release_handler.handle(event);
                    }
                }
        );
        return diagram;
    }

    private void init_line() { // draws default line
        gc.beginPath();
        gc.setLineWidth(line_width);
        gc.setFill(Color.BLACK);
        gc.moveTo(0, height);
        gc.lineTo(canvas_width, height);
        gc.stroke();
        gc.stroke(); // second stroke makes it more solid
    }

    public void reconstruct() { // todo: implement
        // create new canvas, draw lines
        // create new graphics context
    }
}
