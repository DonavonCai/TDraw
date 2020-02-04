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

import java.util.Collections;
import java.util.ArrayList;

class DSignal {
    /* Member Variables */
    // layout
    private int height;
    private int canvas_width;
    private int line_width;
    private Canvas signal;

    // direction checking
    private int prev_mouse_coord;
    public enum Direction {LEFT, RIGHT}
    public enum H_Position{HIGH, LOW}
    private Direction previous_direction;
    private int dir_change;

    // edge tracking
    private int click_edge;
    private ArrayList<Integer> pos_edges;
    private ArrayList<Integer> neg_edges;

    DSignal() {
        height = 30;
        canvas_width = 500;
        line_width = 3;
        prev_mouse_coord = -1;
        signal = new Canvas(canvas_width, height);
        dir_change = -1;
        pos_edges = new ArrayList<>();
        neg_edges = new ArrayList<>();
        System.out.println("Signal created!");
    }

    HBox draw() { // initializes all elements required for DSignal, including buttons, canvas, event handlers, etc.
        Button delete_signal = new Button("X");
        TextField name = new TextField("Signal_Name");

        GraphicsContext gc = signal.getGraphicsContext2D();

        // style the pane instead of the canvas
        Pane signalPane = new Pane(signal);
        signalPane.setPrefSize(canvas_width, height);
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
                            pos_edges.add(click_edge);
                            Collections.sort(pos_edges);
                        }
                        else if (event.getButton() == MouseButton.SECONDARY) {
                            draw_vertical(gc, (int)event.getX());
                            click_edge = (int)event.getX();
                            neg_edges.add(click_edge);
                            Collections.sort(neg_edges);
                        }
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        H_Position h_line_position = H_Position.LOW;
                        Direction current_direction;
                        // get mouse direction
                        if ((prev_mouse_coord > 0) && ((int)event.getX() < prev_mouse_coord)) { // moving left
                            current_direction = Direction.LEFT;
                            if (current_direction != previous_direction) {
                                dir_change = (int)event.getX();
                            }
                            if (event.getButton() == MouseButton.PRIMARY) {
                                h_line_position = H_Position.HIGH;
                            } else if (event.getButton() == MouseButton.SECONDARY) {
                                h_line_position = H_Position.LOW;
                            }
                            draw_horizontal(gc, (int) event.getX(), click_edge, h_line_position, current_direction);
                            previous_direction = Direction.LEFT;
                        }

                        else if ((prev_mouse_coord > 0) && ((int)event.getX() > prev_mouse_coord)) { // moving right
                            current_direction = Direction.RIGHT;
                            if (current_direction != previous_direction) {
                                dir_change = (int)event.getX();

                            }
                            if (event.getButton() == MouseButton.PRIMARY) { // draw high
                                h_line_position = H_Position.HIGH;
                            } else if (event.getButton() == MouseButton.SECONDARY) { // draw low
                                h_line_position = H_Position.LOW;
                            }
                            draw_horizontal(gc, (int) event.getX(), click_edge, h_line_position, current_direction);
                            previous_direction = Direction.RIGHT;
                        }
                        prev_mouse_coord = (int)event.getX();
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        dir_change = -1;
                        if (event.getButton() == MouseButton.PRIMARY) {
                            neg_edges.add((int)event.getX());
                        }
                        else if (event.getButton() == MouseButton.SECONDARY) {
                            pos_edges.add((int)event.getX());
                        }
                    }
                }
        );
        return diagram;
    }

    private void init_line(GraphicsContext g) { // draws default line
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

    private void draw_horizontal(GraphicsContext g, int coord, int respective_edge, H_Position h_pos, Direction current_direction) {
        boolean draw_high = (h_pos == H_Position.HIGH);

        g.setStroke(Color.BLACK);
        g.setLineWidth(line_width);
        g.beginPath();

        if (draw_high) {
            g.moveTo(respective_edge, 0);
            g.lineTo(coord, 0);
            g.stroke();
        }
        else {
            g.moveTo(respective_edge, height);
            g.lineTo(coord, height);
            g.stroke();
        }

        // if edge can be drawn
        draw_vertical(g, coord);

        if (draw_high) { // erase low signal
            g.setFill(Color.WHITE);
            if (current_direction == Direction.LEFT) {
                g.fillRect(coord, line_width, respective_edge - coord, height);
            }
            else {
                g.fillRect(respective_edge, line_width, coord - respective_edge, height);
            }
        }
        else {
            g.setFill(Color.WHITE);
            if (current_direction == Direction.LEFT) {
                g.fillRect(coord, 0, respective_edge - coord, height - line_width);
            }
            else {
                g.fillRect(respective_edge, 0, coord - respective_edge, height - line_width);
            }
        }
    }
}
