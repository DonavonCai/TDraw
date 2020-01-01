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

//import javafx.scene.shape.Line;

public class DSignal { // TODO: get coordinates on mouse click
    private int coord;

    public DSignal() {
        coord = 0;
        System.out.println("Signal created!");
    }
    public HBox draw() {
        // System.out.println("drawing");
        Button delete_signal = new Button("X");
        TextField name = new TextField("Signal_Name");

        Canvas signal = new Canvas(500, 50);
        GraphicsContext gc = signal.getGraphicsContext2D();

        // style the pane instead of the canvas
        Pane signalPane = new Pane(signal);
        signalPane.setPrefSize(500,50);

//         use to test pane sizing
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

        return diagram;
    }

    private void init_line(GraphicsContext g) {
        g.beginPath();
        g.moveTo(0,50);
        g.lineTo(600,50);
        g.stroke();
    }

    public int getCoord() {
        return coord;
    }
}
