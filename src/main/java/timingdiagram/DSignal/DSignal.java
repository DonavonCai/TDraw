package timingdiagram.DSignal;

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
    protected final int height = 30;
    protected final int canvas_width = 700;
    protected final int line_width = 3;

    transient private HBox diagram;
    transient private Pane signalPane;
    transient private Canvas signal;
    transient protected GraphicsContext gc;

    transient private TextField name;

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

    public HBox getDiagram() { return diagram; }

    public DSignal() {
        // layout
        signal = new Canvas();
        signalPane = new Pane();
        gc = signal.getGraphicsContext2D();
        name = new TextField();
        diagram = new HBox();
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

        style();
        format();
        activateEventHandlers();
        init_line();
    }

    private void style() {
        name.setStyle("-fx-background-color: white;");
        name.setAlignment(Pos.BOTTOM_RIGHT);
        name.setMaxWidth(100.0);

        signal.setWidth(canvas_width);
        signal.setHeight(height);
        signalPane.setPrefSize(canvas_width, height);

        diagram.setSpacing(5);
        diagram.setAlignment(Pos.BOTTOM_CENTER);
    }

    private void format() {
        signalPane.getChildren().add(signal);
        name.setText("Signal_Name");
        diagram.getChildren().add(name);
        diagram.getChildren().add(signalPane);
    }

    public void activateEventHandlers() {
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

    // Initialize transient fields.
    public void redraw() {
        diagram = new HBox();
        signalPane = new Pane();
        signal = new Canvas();
        gc = signal.getGraphicsContext2D();
        name = new TextField();
        style();
        format();
        // Event handlers and direction tracker are serializable, don't have to create new.
        activateEventHandlers();
        draw_from_save();
    }

    private void draw_from_save() {
        // note: assume edges are balanced
        for (int i = 0; i < pos_edges.size(); i++) {
            draw_vertical(pos_edges.get(i));
            draw_vertical(neg_edges.get(i));
            // todo: fill in horizontal lines
        }
    }

    protected void draw_vertical(int coord) {
        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(line_width);
        gc.moveTo(coord, height);
        gc.lineTo(coord, 0);
        gc.stroke();
    }
}
