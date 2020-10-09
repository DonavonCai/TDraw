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
import timingdiagram.SignalController.SignalController;

public class DSignal implements Serializable {
    // controller
    SignalController signalController;
    // layout
    private final int name_width = 100;

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

    public DSignal(SignalController s) {
        // todo: implement setWidth function, call this function from SignalController.add_signal()?
        signalController = s;
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

    public void setCanvasWidth(double w) {
        signal.setWidth(w);
        signalPane.setPrefWidth(w);
    }

    private void style() {
        int canvasWidth = signalController.getCanvasWidth();
        int height = signalController.SIGNAL_HEIGHT;
        name.setStyle("-fx-background-color: white;");
        name.setAlignment(Pos.BOTTOM_RIGHT);
        name.setMaxWidth(name_width);

        signal.setWidth(canvasWidth);
        signal.setHeight(height);
        signalPane.setPrefSize(canvasWidth, height);

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
        int height = signalController.SIGNAL_HEIGHT;
        int canvasWidth = signalController.getCanvasWidth();

        gc.beginPath();
        gc.setLineWidth(signalController.LINE_WIDTH);
        gc.setFill(Color.BLACK);
        gc.moveTo(0, height);
        gc.lineTo(canvasWidth, height);
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
        init_line();
        for (int i = 0; i < pos_edges.size(); i++) {
            int pos = pos_edges.get(i);
            int neg = neg_edges.get(i);
            draw_vertical(pos_edges.get(i));
            draw_vertical(neg_edges.get(i));
            draw_high(pos, neg, DirectionTracker.Direction.RIGHT);
        }
    }

    protected void draw_vertical(int coord) {
        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(signalController.LINE_WIDTH);
        gc.moveTo(coord, signalController.SIGNAL_HEIGHT);
        gc.lineTo(coord, 0);
        gc.stroke();
    }

    protected void draw_low(int from, int to, DirectionTracker.Direction direction) {
        int height = signalController.SIGNAL_HEIGHT;
        int lineWidth = signalController.LINE_WIDTH;
        // draw the line
        gc.beginPath();
        gc.moveTo(from, height);
        gc.lineTo(to, height);
        gc.stroke();

        // erase stuff above the line
        int rect_x;
        int rect_y = 0;
        int rect_width;
        int rect_height = height - lineWidth + 1;
        gc.setFill(Color.WHITE);

        if (direction == DirectionTracker.Direction.LEFT) { // erase stuff to right
            rect_x = to;
            rect_width = from - to;
        }
        else { // erase stuff to left
            rect_x = from;
            rect_width = to - from;
        }
        gc.fillRect(rect_x, rect_y, rect_width, rect_height);
    }

    protected void draw_high(int from, int to, DirectionTracker.Direction direction) {
        int lineWidth = signalController.LINE_WIDTH;
        // draw the line
        gc.beginPath();
        gc.moveTo(from, 0);
        gc.lineTo(to, 0);
        gc.stroke();

        // erase stuff below the line
        int rect_x;
        int rect_y = lineWidth - 1;
        int rect_width;
        int rect_height = signalController.SIGNAL_HEIGHT;
        gc.setFill(Color.WHITE);

        if (direction == DirectionTracker.Direction.LEFT) { // erase right
            rect_x = to;
            rect_width = from - to;
        }
        else { // erase left
            rect_x = from;
            rect_width = to - from;
        }
        gc.fillRect(rect_x, rect_y, rect_width, rect_height);
    }
}
