package Model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

class SignalTest {
    @Test
    @DisplayName("Set Left and Right Edges")
    void createSignal() {
        Signal test = new Signal();
        int coord = 50;
        Edge left = new Edge(Edge.Type.POS, coord);
        Edge right = new Edge(Edge.Type.POS, coord + 50);

        test.SetLeftEdge(left);
        test.SetRightEdge(right);

        assertEquals(left, test.GetLeftEdge());
        assertEquals(right, test.GetRightEdge());
    }

    @Test
    @DisplayName("Initial drag right use case")
    void dragRight() {
        Signal test = new Signal();
        int start = 50;
        // On a mouse press, create both edges at the same coord?
        Edge left = new Edge(start);
        Edge right = new Edge(start);

        test.SetLeftEdge(left);
        test.SetRightEdge(right);

        int end = 100;
        test.SetRightCoord(end);
        test.SetLeftType(Edge.Type.POS);
        test.SetRightType(Edge.Type.NEG);

        Edge expectedLeft = new Edge(Edge.Type.POS, start);
        Edge expectedRight = new Edge(Edge.Type.NEG, end);

        assertEquals(expectedLeft, test.GetLeftEdge());
        assertEquals(expectedRight, test.GetRightEdge());
    }

    @Nested
    @DisplayName("Types")
    class types {
        @Test
        @DisplayName("High signal")
        void highSignal() {
            Signal test = new Signal(50);

            test.SetType(Signal.Type.HIGH);
            assertEquals(Edge.Type.POS, test.GetLeftEdge().GetType());
            assertEquals(Edge.Type.NEG, test.GetRightEdge().GetType());
        }

        @Test
        @DisplayName("Low signal")
        void lowSignal() {
            Signal test = new Signal(50);

            test.SetType(Signal.Type.LOW);
            assertEquals(Edge.Type.NEG, test.GetLeftEdge().GetType());
            assertEquals(Edge.Type.POS, test.GetRightEdge().GetType());
        }
    }

}