package timingdiagram;

import javafx.scene.shape.Line;

public class DSignal {
    private int coord;

    public DSignal() {
        coord = 0;
        System.out.println("Signal created!");
    }
    public Line draw() {
        System.out.println("drawing");
        return new Line(100, 10, 10, 10);
    }
    public int getCoord() {
        return coord;
    }
}
