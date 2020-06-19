package timingdiagram;

import javafx.geometry.Pos;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;

class DSignal {
    // enums
    public enum Direction {LEFT, RIGHT, NULL}
    public enum H_Position{HIGH, LOW}

    // layout
    protected final int height;
    protected final int canvas_width;
    protected final int line_width;
    private final Canvas signal;
    protected final GraphicsContext gc;

    // direction checking
    protected int prev_mouse_coord;
    protected H_Position h_line_position;
    protected Direction previous_direction;
    protected Direction current_direction;
    protected Direction initial_direction;

    // event handling
    protected boolean release_edge_pos;
    private final MousePressHandler press_handler;
    private final MouseDragHandler drag_handler;
    private final MouseReleaseHandler release_handler;

    // edge tracking
    protected int current_edge;
    protected int initial_edge;
//    protected boolean erase_edge;
    protected final ArrayList<Integer> pos_edges;
    protected final ArrayList<Integer> neg_edges;

    DSignal() {
        // layout
        height = 30;
        canvas_width = 700;
        line_width = 3;
        signal = new Canvas(canvas_width, height);
        gc = signal.getGraphicsContext2D();

        // direction checking
        prev_mouse_coord = -1;
        previous_direction = Direction.NULL;
        current_direction = Direction.NULL;
        initial_direction = Direction.NULL;
        h_line_position = H_Position.LOW;
        // event handling
        release_edge_pos = false;
        press_handler = new MousePressHandler(this);
        drag_handler = new MouseDragHandler(this);
        release_handler = new MouseReleaseHandler(this);
        // data
        pos_edges = new ArrayList<>();
        neg_edges = new ArrayList<>();
    }

    HBox draw() { // initializes all elements required for DSignal, including buttons, canvas, event handlers, etc.
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
                new EventHandler<>() {
                    @Override
                    public void handle(MouseEvent event) { // code is repeated to avoid event handling with mouse buttons other than left and right click
                        press_handler.handle(event);
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<>() {
                    @Override
                    public void handle(MouseEvent event) {
                        drag_handler.handle(event);
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<>() {
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
}
