package timingdiagram;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import timingdiagram.*;

import javafx.embed.swing.JFXPanel;
import javafx.application.Platform;

import javafx.scene.layout.HBox;

class DSignalTest {

    DSignal test = null;

    @BeforeEach
    public void setup() {
        System.out.println("initializing toolkit");
        /* We do it before each because before class literally doesn't work.
        * Trying to get JUnit to work with JavaFX has been one of the most excruciatingly painful
        * experiences I've ever had. I no longer have the will to live. */
        new JFXPanel();
        System.out.println("creating DSignal");
        test = new DSignal();
    }

    @Test
    public void test_draw() {
        System.out.println("DSignal test: draw");
        HBox h = new HBox(test.draw());

    }
}