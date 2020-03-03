package timingdiagram;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import javafx.scene.layout.HBox;

import static org.junit.Assert.*;

public class DSignalTest {

//    static DSignal test = null;

    @BeforeClass
    public static void setup() {
        System.out.println("Beginning DSignal tests:");
    }

    @Test
    public void test_draw() {
        System.out.println("test: draw");
        DSignal test = new DSignal();
        HBox h = new HBox(test.draw());
        assertNotEquals(h, null);
        h = null;
    }

    @AfterClass
    public static void cleanup() {
        System.out.println("DSignal tests finished.");
    }
}