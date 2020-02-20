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
    // enums
    public enum Direction {LEFT, RIGHT, NULL}
    public enum H_Position{HIGH, LOW}

    // layout
    private int height;
    private int canvas_width;
    private int line_width;
    private Canvas signal;
    protected GraphicsContext gc;

    // direction checking
    protected int prev_mouse_coord;
    protected H_Position h_line_position;
    protected Direction previous_direction;
    protected Direction current_direction;
    protected Direction initial_direction;
    protected boolean moving_backwards;

    // event handling
    private MousePressHandler press_handler;
    private MouseDragHandler drag_handler;
    private MouseReleaseHandler release_handler;

    // edge tracking
    protected int current_edge;
    protected int click_edge_to_add;
    protected boolean erase_edge;
    protected ArrayList<Integer> pos_edges;
    protected ArrayList<Integer> neg_edges;

    DSignal() {
        System.out.println("Signal created!");
        // layout
        height = 30;
        canvas_width = 500;
        line_width = 3;
        signal = new Canvas(canvas_width, height);
        gc = signal.getGraphicsContext2D();

        // direction checking
        prev_mouse_coord = -1;
        previous_direction = Direction.NULL;
        current_direction = Direction.NULL;
        initial_direction = Direction.NULL;
        moving_backwards = false;
        erase_edge = false;
        h_line_position = H_Position.LOW;
        // event handling
        press_handler = new MousePressHandler(this);
        drag_handler = new MouseDragHandler(this);
        release_handler = new MouseReleaseHandler(this);
        // data
        pos_edges = new ArrayList<>();
        neg_edges = new ArrayList<>();
    }

    HBox draw() { // initializes all elements required for DSignal, including buttons, canvas, event handlers, etc.
        Button delete_signal = new Button("X");
        TextField name = new TextField("Signal_Name");

        // style the pane instead of the canvas
        Pane signalPane = new Pane(signal);
        signalPane.setPrefSize(canvas_width, height);
        init_line();

        HBox diagram = new HBox(delete_signal, name, signalPane);

        diagram.setPadding(new Insets(10));
        diagram.setSpacing(5);
        diagram.setAlignment(Pos.BOTTOM_CENTER);

        signalPane.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) { // code is repeated to avoid event handling with mouse buttons other than left and right click
                        press_handler.handle(event);
//
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
//                        System.out.println("initial direction: " + initial_direction);
                        // get mouse direction
                        if ((prev_mouse_coord > 0) && ((int)event.getX() < prev_mouse_coord)) { // moving left
                            if (initial_direction == Direction.NULL) { // set initial direction
                                initial_direction = Direction.LEFT;
                            }
                            current_direction = Direction.LEFT;
                        }
                        else if ((prev_mouse_coord > 0) && ((int)event.getX() > prev_mouse_coord)) { // moving right
                            if (initial_direction == Direction.NULL) {
                                initial_direction = Direction.RIGHT;
                            }
                            current_direction = Direction.RIGHT;
                        }

                        if ((current_direction != previous_direction) && (previous_direction != Direction.NULL)) { // direction change
                            System.out.println("direction change");
                            current_edge = (int)event.getX();
                            moving_backwards = true;
                            if ((current_direction != initial_direction) && (initial_direction != Direction.NULL)) {
                                System.out.println("flipping");
                                h_line_flip();
                                erase_edge = true;
                            }
                            else if (current_direction == initial_direction) {
                                if (moving_backwards) { // returned to initial direction from direction change
                                    System.out.println("flip");
                                    h_line_flip();
                                    erase_edge = true;
                                    moving_backwards = false;
                                }
                            }
                        }
                        if (!in_between_edges((int)event.getX())) { // this vertical line is drawn over in draw_horizontal if mouse continues to be dragged
                            draw_vertical((int)event.getX());
                        }
                        draw_horizontal((int) event.getX(), current_edge, current_direction, erase_edge);
                        previous_direction = current_direction;
                        prev_mouse_coord = (int)event.getX();
                    }
                }
        );

        signalPane.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        moving_backwards = false;
                        previous_direction = Direction.NULL;
                        current_direction = Direction.NULL;
                        initial_direction = Direction.NULL;
                        prev_mouse_coord = -1;
                        if (event.getButton() == MouseButton.PRIMARY) {
//                            pos_edges.add(click_edge_to_add);
//                            Collections.sort(pos_edges);
                            neg_edges.add((int)event.getX());
                            Collections.sort(neg_edges);
                            System.out.println("release: adding neg edge at: " + event.getX());
                        }
                        else if (event.getButton() == MouseButton.SECONDARY) {
//                            neg_edges.add(click_edge_to_add);
//                            Collections.sort(neg_edges);
                            pos_edges.add((int)event.getX());
                            Collections.sort(pos_edges);
                            System.out.println("release: adding pos edge at: " + event.getX());
                        }
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
        gc.stroke(); // second stroke makes it more solid for some reason??? just leave it in until you figure out why
    }

    protected void draw_vertical(int coord) {
        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(line_width);
        gc.moveTo(coord, height);
        gc.lineTo(coord, 0);
        gc.stroke();
    }

    private void draw_horizontal(int coord, int respective_edge, Direction current_direction, boolean erase_respective_edge) { // TODO: only draw vertical if applicable
        boolean draw_high = (h_line_position == H_Position.HIGH);

        gc.beginPath();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(line_width);

        if (draw_high) {
            gc.moveTo(respective_edge, 0);
            gc.lineTo(coord, 0);
            gc.stroke();
        }
        else {
            gc.moveTo(respective_edge, height);
            gc.lineTo(coord, height);
            gc.stroke();
        }

        // erase signal
        int rect_x;
        int rect_y;
        int rect_width;
        int rect_height;

        if (draw_high) {
            gc.setFill(Color.WHITE);
            rect_y = line_width - 1;
            rect_height = height;
            if (current_direction == Direction.LEFT) { // erase right
                rect_x = coord;
                rect_width = respective_edge - coord;
                if (erase_respective_edge) { // erase edge
                    rect_width += line_width;
                }
            }
            else { // erase left
                rect_x = respective_edge;
                rect_width = coord - respective_edge;
                if (erase_respective_edge) { // erase edge
                    rect_x -= line_width;
                    rect_width += line_width;
                }
            }
        }
        else { // draw low
            gc.setFill(Color.WHITE);
            rect_y = 0;
            rect_height = height - line_width + 1;
            if (current_direction == Direction.LEFT) { // erase right
                rect_x = coord;
                rect_width = respective_edge - coord;
                if (erase_respective_edge) { // erase edge
                    rect_width += line_width;
                }
            }
            else { // erase left
                rect_x = respective_edge;
                rect_width = coord - respective_edge;
                if (erase_respective_edge) { // erase edge
                    rect_x -= line_width;
                    rect_width += line_width;
                }
            }
        }
        gc.fillRect(rect_x, rect_y, rect_width, rect_height);
    }

    void h_line_flip() {
        if (h_line_position == H_Position.HIGH) {
            h_line_position = H_Position.LOW;
        }
        else if (h_line_position == H_Position.LOW) {
            h_line_position = H_Position.HIGH;
        }
    }

    boolean in_between_edges(int coord) { // TODO: fix this
        boolean pos_empty = pos_edges.size() == 0;
        boolean neg_empty = neg_edges.size() == 0;
        boolean drawing_high = h_line_position == H_Position.HIGH;
        boolean drawing_low = h_line_position == H_Position.LOW;

        if (pos_empty && neg_empty) { // no edges
            if (drawing_low) {
                System.out.println("neg edge on initial");
                return true;
            }
            return false;
        }
        else {
            if (drawing_low && !pos_empty && coord < pos_edges.get(0) && !moving_backwards) { // low signal before first positive edge, !moving_backwards so you can drag left initially
                System.out.println("low sig before 1st pos edge");
                return true;
            }
            for (int i = 0; (i < pos_edges.size() && (i < neg_edges.size())); i++) {
                if (drawing_high && edges_balanced()) { // avoid the case where we are "in between" because we haven't finished adding an edge through dragging
                    if ((coord >= pos_edges.get(i)) && (coord <= neg_edges.get(i))) { // positive edge placement on high signal
                        System.out.println("pos edge on high signal: in between " + pos_edges.get(i) + "," + neg_edges.get(i));
                        return true;
                    }
                }
                else if (drawing_low && edges_balanced()){
                    if ((coord >= neg_edges.get(i)) && (coord <= pos_edges.get(i))) { // negative edge placement on low signal
                        System.out.println("neg edge on low signal: in between " + pos_edges.get(i) + "," + neg_edges.get(i));
                        return true;
                    }
                }
            }
        }
        return false;
    }

    boolean edges_balanced() {
        return (pos_edges.size() == neg_edges.size());
    }
}
