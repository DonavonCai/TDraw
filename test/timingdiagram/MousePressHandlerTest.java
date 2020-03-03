package timingdiagram;

import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import static org.junit.Assert.assertNotEquals;

public class MousePressHandlerTest {
    @BeforeClass
    public void b() {
        System.out.println("Beginning mouse press handler tests:");
    }
    @Test
    public void leftClick_noEdges() {
        System.out.println("test: draw");
        DSignal test = new DSignal();
        HBox h = new HBox(test.draw());
        MousePressHandler m = new MousePressHandler(test);
        test.setPressHandler(m);
//        MouseEvent e = new MouseEvent(null, null, MouseEvent.MOUSE_PRESSED,
//                50, 16, MouseButton.PRIMARY, 1, false, false, false, false,
//                true, false, false, false, false, false, null);
    }

    @AfterClass
    public void afterPressTest() {
        System.out.println("Finished Mouse Press Handler tests!");
    }
}