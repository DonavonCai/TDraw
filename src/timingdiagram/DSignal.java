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

import java.util.HashMap;
import java.util.ArrayList;

class DSignal { // TODO: handle mouse events
    private int height;
    private int canvas_width;
    private int num_edges;

    // key <= numbered edge from left to right. The first edge is numbered 0.
    // value <= 0 for negative, 1 for positive
    private HashMap<Integer, Boolean> edge_type;
    private HashMap<Integer, Integer> edge_coords;

    DSignal() {
        height = 30;
        canvas_width = 500;
        num_edges = 0;
        edge_type = new HashMap<Integer, Boolean>();
        edge_coords = new HashMap<Integer, Integer>();
        System.out.println("Signal created!");
    }
    HBox draw() {
        // System.out.println("drawing");
        Button delete_signal = new Button("X");
        TextField name = new TextField("Signal_Name");

        Canvas signal = new Canvas(canvas_width, height);
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

        gc.setLineWidth(4.0);
        gc.setFill(Color.BLACK);

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
                            draw_vertical(gc, event.getX());
                            edge_type.put(num_edges, true);
                            edge_coords.put(num_edges, (int)event.getX());
                            num_edges++;

                            // TODO: reorder edge lists

                            System.out.println("num edges: " + num_edges);
                            System.out.println("press at " + event.getX());
                        }
                        else if (event.getButton() == MouseButton.SECONDARY) {
                            draw_vertical(gc, event.getX());
                            edge_type.put(num_edges, false);
                            edge_coords.put(num_edges, (int)event.getX());
                            num_edges++;

                            // TODO: reorder edge lists

                            System.out.println("num edges: " + num_edges);
                            System.out.println("press at " + event.getX());
                        }
                    }
                }
        );

        return diagram;
    }

    private void init_line(GraphicsContext g) {
        g.beginPath();
        g.moveTo(0, height);
        g.lineTo(canvas_width, height);
        g.stroke();
    }

    private void draw_vertical(GraphicsContext g, double coord) { // TODO: decide on intuitive way to insert clock edges
        g.setLineWidth(2.5); // 4.0 makes vertical line too thick
        g.beginPath();
        g.moveTo(coord, height);
        g.lineTo(coord, 0);
        g.stroke();
        g.setLineWidth(4.0);
    }
}
