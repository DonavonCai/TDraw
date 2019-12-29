package timingdiagram;

import javafx.scene.control.TextField; // TODO: make textfield feel like editable label
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;

public class DSignal {
    private int coord;

    public DSignal() {
        coord = 0;
        System.out.println("Signal created!");
    }
    public HBox draw() {
        System.out.println("drawing");
        TextField name = new TextField("Signal_Name");
        Line signal = new Line(100, 10, 10, 10);
        HBox diagram = new HBox(name, signal);
        return diagram;
    }
    public int getCoord() {
        return coord;
    }
}
