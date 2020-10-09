package timingdiagram.SignalController;

import timingdiagram.DSignal.DSignal;

import java.io.Serializable;
import java.util.ArrayList;

class SignalControllerStorage implements Serializable {
    private int canvasWidth;
    protected final int MAX_SIGS = 15;
    protected int num_sigs;
    protected final String IDLE_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #e0e0e0;";
    protected final String PRESSED_BUTTON_STYLE = "-fx-border-width: 1; -fx-border-color: black; -fx-background-color: #949494;";

    protected ArrayList<DSignal> signals;

    SignalControllerStorage() {
        canvasWidth = 700;
        num_sigs = 0;
        signals = new ArrayList<>();
    }

    public int getCanvasWidth() {return canvasWidth;}
    public void setCanvasWidth(int w) {canvasWidth = w;}
}
