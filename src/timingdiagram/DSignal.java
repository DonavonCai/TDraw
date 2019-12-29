package timingdiagram;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;

import javafx.scene.control.Button;

public class DSignal {
    private int coord;

    public DSignal() {
        coord = 0;
        System.out.println("Signal created!");
    }
    public HBox draw() {
        System.out.println("drawing");
        Button delete_signal = new Button("X");
        TextField name = new TextField("Signal_Name");
        Line signal = new Line(100.0, 0, 600.0, 0);
        HBox diagram = new HBox(delete_signal, name, signal);

        diagram.setPadding(new Insets(10));
        diagram.setSpacing(5);
        diagram.setAlignment(Pos.BOTTOM_CENTER);

        return diagram;
    }
    public int getCoord() {
        return coord;
    }
}
