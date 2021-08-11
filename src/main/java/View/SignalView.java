package View;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class SignalView {
    private double canvasWidth = 700;
    // Constants: -------------------------------------------
    public final int MIN_CANVAS_WIDTH = 200;
    public final int MAX_CANVAS_WIDTH = 1000;
    public final int LINE_WIDTH = 3;
    public final int NAME_WIDTH = 100;
    public final int SIGNAL_HEIGHT = 30;

    // FXML: ------------------------------------------------
    private HBox diagram;
    private Pane signalPane;
    private Canvas signal;
    private GraphicsContext gc;
    private TextField name;

    // Accessors: --------------------------------------------
    public HBox GetDiagram() { return diagram; }

    // Interface: --------------------------------------------
    public SignalView() {
        // layout
        signal = new Canvas();
        signalPane = new Pane();
        gc = signal.getGraphicsContext2D();
        name = new TextField();
        diagram = new HBox();

        SetStyle();
        SetFormat();
    }

    // Helpers: ----------------------------------------------
    private void SetStyle() {
        double canvasWidth = 700;
        int height = SIGNAL_HEIGHT;
        name.setStyle("-fx-background-color: white;");
        name.setAlignment(Pos.BOTTOM_RIGHT);
        name.setMaxWidth(NAME_WIDTH);

        signal.setWidth(canvasWidth);
        signal.setHeight(height);
        signalPane.setPrefSize(canvasWidth, height);

        diagram.setAlignment(Pos.BOTTOM_CENTER);
    }

    // Set object hierarchy.
    private void SetFormat() {
        signalPane.getChildren().add(signal);
        name.setText("SignalName");
        diagram.getChildren().add(name);
        diagram.getChildren().add(signalPane);
    }
}
