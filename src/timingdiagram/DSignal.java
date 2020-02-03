package timingdiagram;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.event.EventHandler;

class DSignal { // TODO: handle mouse events
    private int height;
    private int canvas_width;
    private int prev_mouse_coord;
    private int click_edge;
    private int drag_edge;
    private int line_width;
    private Canvas signal;
//    private ArrayList<Integer> pos_edges;
//    private ArrayList<Integer> neg_edges;

    DSignal() {
        height = 30;
        canvas_width = 500;
        line_width = 3;
        prev_mouse_coord = -1;
        signal = new Canvas(canvas_width, height);
        drag_edge = -1;
//        pos_edges = new ArrayList<>();
//        neg_edges = new ArrayList<>();
        System.out.println("Signal created!");
    }
    HBox draw() {
        Button delete_signal = new Button("X");
        TextField name = new TextField("Signal_Name");

        GraphicsContext gc = signal.getGraphicsContext2D();

        // style the pane instead of the canvas
        Pane signalPane = new Pane(signal);
        signalPane.setPrefSize(canvas_width, height);

        // use to test pane sizing
//        signalPane.setStyle("-fx-padding: 10;" +
//                "-fx-border-style: solid inside;" +
//                "-fx-border-width: 2;" +
//                "-fx-border-radius: 5;" +
//                "-fx-border-color: blue;");
        init_line(gc);

        HBox diagram = new HBox(delete_signal, name, signalPane);

        diagram.setPadding(new Insets(10));
        diagram.setSpacing(5);
        diagram.setAlignment(Pos.BOTTOM_CENTER);

        signalPane.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) { // code is repeated to avoid event handling with mouse buttons other than left and right click
                        if (event.getButton() == MouseButton.PRIMARY) {
                            draw_vertical(gc, (int)event.getX());
                            click_edge = (int)event.getX();
                        }
                        else if (event.getButton() == MouseButton.SECONDARY) {
                            draw_vertical(gc, (int)event.getX());
                            click_edge = (int)event.getX();
                        }
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // get mouse direction
                        if ((prev_mouse_coord > 0) && ((int)event.getX() < prev_mouse_coord)) { // moving left and drag_edge not initialized
                            if (drag_edge == -1) {
                                drag_edge = (int) event.getX();
                            }

                            if (event.getButton() == MouseButton.PRIMARY) {
                                draw_high(gc, (int) event.getX(), drag_edge);
                            } else if (event.getButton() == MouseButton.SECONDARY) {
                                draw_low(gc, (int) event.getX(), drag_edge);
                            }
                        }

                        else if ((prev_mouse_coord > 0) && ((int)event.getX() > prev_mouse_coord)) { // moving right
                            if (event.getButton() == MouseButton.PRIMARY) {
                                draw_high(gc, (int) event.getX(), click_edge);
                            } else if (event.getButton() == MouseButton.SECONDARY) {
                                draw_low(gc, (int) event.getX(), click_edge);
                            }
                        }
                        prev_mouse_coord = (int)event.getX();
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        drag_edge = -1;
                    }
                }
        );

        return diagram;
    }

    private void init_line(GraphicsContext g) {
        g.beginPath();
        g.setLineWidth(line_width);
        g.setFill(Color.BLACK);
        g.moveTo(0, height);
        g.lineTo(canvas_width, height);
        g.stroke();
    }

    private void draw_vertical(GraphicsContext g, int coord) {
        g.setStroke(Color.BLACK);
        g.setLineWidth(line_width);
        g.beginPath();
        g.moveTo(coord, height);
        g.lineTo(coord, 0);
        g.stroke();
    }

    private void draw_high(GraphicsContext g, int coord, int respective_edge) { // TODO: fix drag edge case
//        System.out.println("coord: " + coord);
//        System.out.println("current edge: " + click_edge);
        g.setStroke(Color.BLACK);
        g.setLineWidth(line_width);

        // draw high signal
        g.beginPath();
        g.moveTo(respective_edge, 0);
        g.lineTo(coord, 0);
        g.stroke();

        draw_vertical(g, coord);

        // erase low signal
        g.setFill(Color.WHITE);
        g.fillRect(respective_edge + line_width, line_width, coord - respective_edge - line_width, height - line_width);
    }

    private void draw_low(GraphicsContext g, int coord, int respective_edge) {
        g.setStroke(Color.BLACK);
        g.setLineWidth(line_width);

        // draw low signal
        g.beginPath();
        g.moveTo(respective_edge, height);
        g.lineTo(coord, height);
        g.stroke();

        draw_vertical(g, coord);

        // erase low signal
        g.setFill(Color.WHITE);
        g.fillRect(respective_edge + line_width, 0, coord - respective_edge - line_width, height - line_width);
    }
}
