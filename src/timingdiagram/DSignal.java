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
import javafx.event.EventHandler;

class DSignal { // TODO: handle mouse events
    private int height;
    private int canvas_width;

    DSignal() {
        height = 45;
        canvas_width = 500;
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
                    public void handle(MouseEvent e) {
                        System.out.println("press at " + e.getX());
                        draw_vertical(gc);
                    };
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

    private void draw_vertical(GraphicsContext g) {

    }
}
