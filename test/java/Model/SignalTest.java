package Model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

class SignalTest {
    private Signal signal;
    private Edge leftBound;
    private Edge rightBound;

    @BeforeEach
    void init() {
        signal = new Signal(new ArrayList<Edge>());

        leftBound = new Edge(Edge.Type.NEG, 0);
        rightBound = new Edge(Edge.Type.POS, 1000);
        signal.Init(leftBound, rightBound);
    }

    @DisplayName("Init")
    @Test
    void testOne() {
        Edge left = new Edge(Edge.Type.NEG, 0);
        Edge right = new Edge(Edge.Type.POS, 1000);

        Edge[] expected = {left, right};
        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
        assertArrayEquals(expected, actual);
    }

    @Nested
    @DisplayName("Creating high signals")
    class highSignal {
        @Nested
        @DisplayName("One Signal")
        class oneSignal {
            @DisplayName("Extend right")
            @Test
            void extendRight() {
                int start = 50;
                int end = 100;
                Edge edgeA = new Edge(Edge.Type.POS, start);
                Edge edgeB = new Edge(Edge.Type.NEG, end);

                signal.Anchor(Signal.Type.HIGH, start);

                Edge[] expectedAfterCreate = {leftBound, edgeA, rightBound};
                Edge[] actualAfterCreate = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expectedAfterCreate, actualAfterCreate);

                // Simulates dragging
                for (int i = start; i <= end; i++) {
                    signal.Extend(i);
                }

                // Test for edges:
                Edge[] expectedAfterInsert = {leftBound, edgeA, edgeB, rightBound};
                Edge[] actualAfterInsert = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expectedAfterInsert, actualAfterInsert);
            }

            @DisplayName("Extend existing right")
            @Test
            void extendExistingRight() {
                int start = 50;
                int end = 75;

                signal.Anchor(Signal.Type.HIGH, start);
                // Simulates dragging
                for (int i = start; i <= end; i++) {
                    signal.Extend(i);
                }

                Edge edgeA = new Edge(Edge.Type.POS, start);
                Edge edgeB = new Edge(Edge.Type.NEG, end);
                // Test for edges:
                Edge[] expectedAfterInsert = {leftBound, edgeA, edgeB, rightBound};
                Edge[] actualAfterInsert = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expectedAfterInsert, actualAfterInsert);

                // Drag again
                int newStart = 70;
                int newEnd = 100;
                signal.Anchor(Signal.Type.HIGH, newStart);
                for (int i = newStart; i <= newEnd; i++) {
                    signal.Extend(i);
                }

                edgeB.SetCoord(newEnd);
                // Test for edges:
                Edge[] expectedAfterExtend = {leftBound, edgeA, edgeB, rightBound};
                Edge[] actualAfterExtend = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expectedAfterExtend, actualAfterExtend);
            }

            @DisplayName("Extend left")
            @Test
            void extendLeft() {
                int start = 100;
                int end = 50;
                Edge edgeA = new Edge(Edge.Type.POS, start);
                Edge edgeB = new Edge(Edge.Type.NEG, end);

                signal.Anchor(Signal.Type.HIGH, start);

                Edge[] expectedAfterCreate = {leftBound, edgeA, rightBound};
                Edge[] actualAfterCreate = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expectedAfterCreate, actualAfterCreate);

                signal.Flip();
                edgeA.SetType(Edge.Type.NEG);
                edgeB.SetType(Edge.Type.POS);
                Edge[] expectedAfterFlip = {leftBound, edgeA, rightBound};
                Edge[] actualAfterFlip = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expectedAfterFlip, actualAfterFlip);

                // Simulates dragging
                for (int i = start; i >= end; i--) {
                    signal.Extend(i);
                }

                signal.PrintEdges();
                // Test for edges:
                Edge[] expectedAfterInsert = {leftBound, edgeB, edgeA, rightBound};
                Edge[] actualAfterInsert = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expectedAfterInsert, actualAfterInsert);
            }

            @DisplayName("Extend existing left")
            @Test
            void extendExistingLeft() {
            }

            @DisplayName("Delete high signal")
            @Test
            void deleteHighSignal() {
                int start = 50;
                int end = 100;
                Edge edgeA = new Edge(Edge.Type.POS, start);
                Edge edgeB = new Edge(Edge.Type.NEG, end);

                signal.Anchor(Signal.Type.HIGH, start);
                // Simulates dragging
                for (int i = start; i <= end; i++) {
                    signal.Extend(i);
                }

                Edge[] expectedAfterInsert = {leftBound, edgeA, edgeB, rightBound};
                Edge[] actualAfterInsert = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expectedAfterInsert, actualAfterInsert);

                // Simulate a right-click drag
                start = 30;
                end = 120;

                signal.Anchor(Signal.Type.LOW, start);
                for (int i = start; i <= end; i++) {
                    signal.Extend(i);
                }

                ArrayList<Edge> arr = new ArrayList<Edge>();

                Edge[] expected = {leftBound, rightBound};
                Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expected, actual);
            }
        }

        @Nested
        @DisplayName("Multiple signals")
        class MultipleSignals {
            @Test
            @DisplayName("Merge high signals")
            void mergeHighSignals() {
                int start1 = 25;
                int end1 = 50;
                int start2 = 75;
                int end2 = 100;


            }
        }
    }
}