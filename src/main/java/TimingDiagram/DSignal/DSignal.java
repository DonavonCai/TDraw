package TimingDiagram.DSignal;

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
import java.util.Collections;

import TimingDiagram.SignalController.SignalController;
import TimingDiagram.DSignal.Edge.Edge;

public class DSignal implements Serializable {
// Data, Constructor: ----------------------------------------------------------
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
    private final DragBoundsTracker dragBoundsTracker;
    // event handling
    protected boolean isDragging;
    private final MousePressHandler press_handler;
    private final MouseDragHandler drag_handler;
    private final MouseReleaseHandler release_handler;

    // Used for comparison
    protected Edge curEdge;
    // Edge created on mouse press
    protected Edge pressEdge;
    // Non-release edge to add
    protected Edge QLeftEdge;
    protected Edge QRightEdge;

    protected final ArrayList<Edge> edges;

    public DSignal(SignalController s) {
        signalController = s;
        // layout
        signal = new Canvas();
        signalPane = new Pane();
        gc = signal.getGraphicsContext2D();
        name = new TextField();
        diagram = new HBox();
        // direction checking
        directionTracker = new DirectionTracker();
        dragBoundsTracker = new DragBoundsTracker();
        // event handling
        isDragging = false;
        press_handler = new MousePressHandler(this, directionTracker, dragBoundsTracker);
        drag_handler = new MouseDragHandler(this, directionTracker, dragBoundsTracker);
        release_handler = new MouseReleaseHandler(this, directionTracker, dragBoundsTracker);

        curEdge = new Edge();
        pressEdge = new Edge();
        edges = new ArrayList<>();

        style();
        format();
        activateEventHandlers();
        init_line();
    }

// Public interface: ---------------------------------------------------------------------------
    // Diagram is what we want to export as an image file.
    public HBox getDiagram() {
        return diagram;
    }

    public void setCanvasWidth(double w) {
        // todo: instead of setting it here, should these be bound to SignalControllerStorage.canvasWidth?
        signal.setWidth(w);
        signalPane.setPrefWidth(w);

        if (w <signalController.getCanvasWidth())
            shortenLineTo(w);
        else if (w > signalController.getCanvasWidth())
            extendLineTo(w);
    }

    public double getCanvasWidth() {
        return signalPane.getWidth();
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

// Protected functions: --------------------------------------------------------------

    // Gets index of edge in DSignal.edges
    protected int edgeIndexOf(Edge e) {
        double coord = e.getCoord();
        Edge.Type type = e.getType();
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getCoord() == coord && edges.get(i).getType() == type)
                return i;
        }
        return -1;
    }

    protected void addEdgeAndSort(Edge e) {
        if (e.getLocation() == Edge.Location.START) {
            edges.remove(0);
        }
        else if (e.getLocation() == Edge.Location.END) {
            System.out.println("adding end edge");
            // todo: make sure this works
//            removeNonDummyEndEdge();
        }

        if (e.getCoord() < 0) {
            e.setCoord(-1);
        }
        else if (e.getCoord() > getCanvasWidth()) {
            e.setCoord(getCanvasWidth() + 1);
            System.out.println("corrected coord");
        }

        Edge deepCopy = new Edge(e);

        edges.add(deepCopy);
        sortEdges();
    }

    protected void sortEdges() {
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < edges.size() - 1; i++) {
                if (edges.get(i).getCoord() > edges.get(i + 1).getCoord()) {
                    Collections.swap(edges, i, i + 1);
                    sorted = false;
                }
            }
        }
    }

    protected void printEdges() {
        for (int i = 0; i < edges.size(); i++) {
            System.out.println("-----" );
            System.out.println("index: " + i);
            System.out.println("coord: " + edges.get(i).getCoord());
            System.out.println("type: " + edges.get(i).getType());
            System.out.println("location: " + edges.get(i).getLocation());
            System.out.println("-----");
        }
    }

    protected void draw_vertical(double coord) {
        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(signalController.LINE_WIDTH);
        gc.moveTo(coord, signalController.SIGNAL_HEIGHT);
        gc.lineTo(coord, 0);
        gc.stroke();
    }

    protected void draw_low(double from, double to, DirectionTracker.Direction direction) {
        int height = signalController.SIGNAL_HEIGHT;
        int lineWidth = signalController.LINE_WIDTH;
        // draw the line
        gc.beginPath();
        gc.moveTo(from, height);
        gc.lineTo(to, height);
        gc.stroke();

        // erase stuff above the line
        double rect_x;
        int rect_y = 0;
        double rect_width;
        double rect_height = height - lineWidth + 1;
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

    protected void draw_high(double from, double to, DirectionTracker.Direction direction) {
        int lineWidth = signalController.LINE_WIDTH;
        // draw the line
        gc.beginPath();
        gc.moveTo(from, 0);
        gc.lineTo(to, 0);
        gc.stroke();

        // erase stuff below the line
        double rect_x;
        double rect_y = lineWidth - 1;
        double rect_width;
        double rect_height = signalController.SIGNAL_HEIGHT;
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

// Helper Functions: -----------------------------------------------------------------
    // Draws default line when application is first launched.
    private void init_line() {
        int height = signalController.SIGNAL_HEIGHT;
        double canvasWidth = signalController.getCanvasWidth();

        gc.beginPath();
        gc.setLineWidth(signalController.LINE_WIDTH);
        gc.setFill(Color.BLACK);
        gc.moveTo(0, height);
        gc.lineTo(canvasWidth, height);
        gc.stroke();
        gc.stroke(); // second stroke makes it more solid


        edges.add(new Edge(Edge.Type.NEG, Edge.Location.START, -10));
        edges.add(new Edge(Edge.Type.POS, Edge.Location.END, canvasWidth + 10));
    }

    private void style() {
        double canvasWidth = signalController.getCanvasWidth();
        int height = signalController.SIGNAL_HEIGHT;
        name.setStyle("-fx-background-color: white;");
        name.setAlignment(Pos.BOTTOM_RIGHT);
        name.setMaxWidth(name_width);

        signal.setWidth(canvasWidth);
        signal.setHeight(height);
        signalPane.setPrefSize(canvasWidth, height);

        diagram.setAlignment(Pos.BOTTOM_CENTER);
    }

    // Set object hierarchy.
    private void format() {
        signalPane.getChildren().add(signal);
        name.setText("Signal_Name");
        diagram.getChildren().add(name);
        diagram.getChildren().add(signalPane);
    }

    // Draws lines on canvas according to saved edges.
    private void draw_from_save() {
        // note: assume edges are balanced
//        init_line();
//        for (int i = 0; i < pos_edges.size(); i++) {
//            int pos = pos_edges.get(i);
//            int neg = neg_edges.get(i);
//            draw_vertical(pos_edges.get(i));
//            draw_vertical(neg_edges.get(i));
//            draw_high(pos, neg, DirectionTracker.Direction.RIGHT);
//        }

        Edge cur, next;
        for (int i = 0; i < edges.size(); i++) {
            // todo: check if this is correct
            cur = edges.get(i);
            if (cur.getLocation() == Edge.Location.MID) { // edge should be visible
                draw_vertical(cur.getCoord());
            }
            else if (cur.getLocation() == Edge.Location.END) {
                return;
            }

            next = edges.get(i + 1);
            if (cur.getType() == Edge.Type.NEG) {
                draw_low(cur.getCoord(), next.getCoord(), DirectionTracker.Direction.LEFT);
            }
            else { // positive edge
                draw_high(cur.getCoord(), next.getCoord(), DirectionTracker.Direction.RIGHT);
            }
        }
    }

    private void removeNonDummyEndEdge() {
        System.out.println("searching");
        for (int i = edges.size() - 1; i >= 0; i--) {
            if (edges.get(i).getLocation() == Edge.Location.END /*&& edges.get(i).getType() != Edge.Type.DUMMY*/) {
                System.out.println("removed");
                edges.remove(i);
                return;
            }
        }
    }

    // Extends the line to coordinate x according to rightmost edge, adds closing edge just outside of visible canvas.
    private void extendLineTo(double x) {
        // todo: get last edge, check type, extend line, move closing edge.
    }

    // Shortens line to x, removing any edges to the right of x.
    // todo: this will probably affect edges_balanced. Maybe try putting an invisible closing edge at the edge of the canvas?
    private void shortenLineTo(double x) {
        // todo: get closest left edge, remove all right edges, add closing edge.
    }

    private void activateEventHandlers() {
        signalPane.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
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
}
