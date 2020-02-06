package timingdiagram;

import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
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
import org.w3c.dom.ls.LSOutput;

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
    public enum Direction {LEFT, RIGHT, NULL}
    public enum H_Position{HIGH, LOW}
    private H_Position h_line_position;
    private Direction previous_direction;
    private Direction current_direction;
    private Direction initial_direction;
    private boolean dir_change;

    // edge tracking
    private int current_edge;
    private ArrayList<Integer> pos_edges;
    private ArrayList<Integer> neg_edges;

    DSignal() {
        height = 30;
        canvas_width = 500;
        line_width = 3;
        prev_mouse_coord = -1;
        previous_direction = Direction.NULL;
        current_direction = Direction.NULL;
        initial_direction = Direction.NULL;
        signal = new Canvas(canvas_width, height);
        dir_change = false;
        h_line_position = H_Position.LOW;
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
                            previous_direction = Direction.NULL;
                            h_line_position = H_Position.HIGH;
                            draw_vertical(gc, (int)event.getX());
                            current_edge = (int)event.getX();
                            pos_edges.add(current_edge);
                            Collections.sort(pos_edges);
                        }
                        else if (event.getButton() == MouseButton.SECONDARY) {
                            previous_direction = Direction.NULL;
                            h_line_position = H_Position.LOW;
                            draw_vertical(gc, (int)event.getX());
                            current_edge = (int)event.getX();
                            neg_edges.add(current_edge);
                            Collections.sort(neg_edges);
                        }
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // TODO: flip when respective edge is dragged over after direction change
                        // TODO: delete edges that are dragged over
//                        System.out.println("initial direction: " + initial_direction);
                        // get mouse direction
                        if ((prev_mouse_coord > 0) && ((int)event.getX() < prev_mouse_coord)) { // moving left
                            if (initial_direction == Direction.NULL) {
                                initial_direction = Direction.LEFT;
                            }
                            current_direction = Direction.LEFT;
                            if ((current_direction != previous_direction) && (current_direction != Direction.NULL) && (previous_direction != Direction.NULL)) {
                                current_edge = (int)event.getX();
                                dir_change = true;
                                System.out.println("direction change: " + dir_change);
                                // if the click edge has not been reached
                                if ((current_direction != initial_direction) && (initial_direction != Direction.NULL)) {
                                    System.out.println("flipping");
                                    h_line_flip();
                                }
                                else if (current_direction == initial_direction) {
                                    if (dir_change) { // returned to initial direction from direction change
                                        h_line_flip();
                                    }

                                    dir_change = false;
                                }
                            }
                            draw_horizontal(gc, (int) event.getX(), current_edge, h_line_position, current_direction);
                            previous_direction = Direction.LEFT;
                        }

                        else if ((prev_mouse_coord > 0) && ((int)event.getX() > prev_mouse_coord)) { // moving right
                            if (initial_direction == Direction.NULL) {
                                initial_direction = Direction.RIGHT;
                            }
                            current_direction = Direction.RIGHT;
                            if ((current_direction != previous_direction) && (previous_direction != Direction.NULL)) {
                                current_edge = (int)event.getX();
                                dir_change = true;
                                System.out.println("direction change: " + dir_change);
                                if ((current_direction != initial_direction) && (initial_direction != Direction.NULL)) {
                                    System.out.println("flipping");
                                    h_line_flip();
                                }
                                else if (current_direction == initial_direction) {
                                    if (dir_change) { // returned to initial direction from direction change
                                        h_line_flip();
                                    }
                                    dir_change = false;
                                }
                            }
                            draw_horizontal(gc, (int) event.getX(), current_edge, h_line_position, current_direction);
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
                        dir_change = false;
                        previous_direction = Direction.NULL;
                        current_direction = Direction.NULL;
                        initial_direction = Direction.NULL;
                        prev_mouse_coord = -1;
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

    private void draw_horizontal(GraphicsContext g, int coord, int respective_edge, H_Position h_pos, Direction current_direction) { // TODO: erase edge after direction change
//        System.out.println("current dirction: " + current_direction);
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

        // erase signal
        int rect_x;
        int rect_y;
        int rect_width;
        int rect_height;
        if (draw_high) {
            g.setFill(Color.WHITE);
            rect_y = line_width;
            rect_height = height;
            if (current_direction == Direction.LEFT) { // erase right
                rect_x = coord;
                rect_width = respective_edge - coord;
//                if (dir_change) { // moving left after direction change
////                    System.out.println("direction change");
//                    rect_width += line_width;
//                }
            }
            else {
                rect_x = respective_edge;
                rect_width = coord - respective_edge;
            }
        }
        else {
            g.setFill(Color.WHITE);
            rect_y = 0;
            rect_height = height - line_width;
            if (current_direction == Direction.LEFT) {
                rect_x = coord;
                rect_width = respective_edge - coord;
            }
            else {
                rect_x = respective_edge;
                rect_width = coord - respective_edge;
            }
        }
        g.fillRect(rect_x, rect_y, rect_width, rect_height);
    }

    void h_line_flip() {
        if (h_line_position == H_Position.HIGH) {
            h_line_position = H_Position.LOW;
        }
        else if (h_line_position == H_Position.LOW) {
            h_line_position = H_Position.HIGH;
        }
    }
}
