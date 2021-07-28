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
            @Nested
            @DisplayName("Drag Right")
            class dragRight {
                @DisplayName("Anchor")
                @Test
                void testAnchor() {
                    int start = 50;
                    Edge edgeA = new Edge(Edge.Type.POS, start);

                    signal.Anchor(Signal.Type.HIGH, start);

                    Edge[] expectedAfterCreate = {leftBound, edgeA, rightBound};
                    Edge[] actualAfterCreate = signal.GetEdges().toArray(new Edge[0]);
                    assertArrayEquals(expectedAfterCreate, actualAfterCreate);
                }

                @Nested
                @DisplayName("Dragging")
                class testDrag {
                    int start;
                    int end;
                    Edge edgeA;
                    Edge edgeB;

                    @BeforeEach
                    void createHighSignal() {
                        start = 50;
                        end = 100;

                        // Record the edges for testing
                        edgeA = new Edge(Edge.Type.POS, start);
                        edgeB = new Edge(Edge.Type.NEG, end);

                        signal.Anchor(Signal.Type.HIGH, start);
                        // Simulates dragging
                        for (int i = start; i <= end; i++) {
                            signal.Extend(i);
                        }
                    }

                    @DisplayName("Right drag create")
                    @Test
                    void testSignalCreation() {
                        // Test for edges:
                        Edge[] expectedAfterInsert = {leftBound, edgeA, edgeB, rightBound};
                        Edge[] actualAfterInsert = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expectedAfterInsert, actualAfterInsert);
                    }

                    @DisplayName("Extend right")
                    @Test
                    void extendRight() {
                        // Drag again
                        int newStart = 90;
                        int newEnd = 150;
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
                        // Drag again
                        int newStart = 60;
                        int newEnd = 10;
                        signal.Anchor(Signal.Type.HIGH, newStart);
                        signal.SetDirectionLeft();
                        for (int i = newStart; i >= newEnd; i--) {
                            signal.Extend(i);
                        }

                        edgeA.SetCoord(newEnd);

                        // Test for edges:
                        Edge[] expectedAfterExtend = {leftBound, edgeA, edgeB, rightBound};
                        Edge[] actualAfterExtend = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expectedAfterExtend, actualAfterExtend);
                    }

                    @DisplayName("Extend right, then left")
                    @Test
                    void extendBoth() {
                        int newStart = 90;
                        int newMid = 150;
                        int newEnd = 10;

                        signal.Anchor(Signal.Type.HIGH, newStart);
                        for (int i = newStart; i <= newMid; i++) {
                            signal.Extend(i);
                        }

                        signal.PrintEdges();

                        signal.Anchor(Signal.Type.HIGH, newMid);
                        signal.SetDirectionLeft();
                        for (int i = newMid; i >= newEnd; i--) {
                            signal.Extend(i);
                        }

                        signal.PrintEdges();

                        edgeA.SetCoord(newEnd);
                        edgeA.SetType(Edge.Type.POS);
                        edgeB.SetCoord(newMid);
                        edgeB.SetType(Edge.Type.NEG);

                        Edge[] expected = {leftBound, edgeA, edgeB, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                        assertArrayEquals(expected, actual);
                    }

                    @DisplayName("Delete right")
                    @Test
                    void deleteRight() {
                        // Simulate a right-click drag over the existing signal
                        start = 30;
                        end = 120;

                        signal.Anchor(Signal.Type.LOW, start);
                        for (int i = start; i <= end; i++) {
                            signal.Extend(i);
                        }

                        Edge[] expected = {leftBound, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expected, actual);
                    }

                    @DisplayName("Delete left")
                    @Test
                    void deleteLeft() {
                        // Simulate a right-click drag over the existing signal
                        start = 120;
                        end = 30;

                        signal.Anchor(Signal.Type.LOW, start);
                        signal.SetDirectionLeft();
                        for (int i = start; i >= end; i--) {
                            signal.Extend(i);
                        }

                        Edge[] expected = {leftBound, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expected, actual);
                    }
                }
            }

            @Nested
            @DisplayName("Drag Left")
            class dragLeft {
                @Test
                @DisplayName("Anchor")
                void testAnchor() {
                    int start = 100;
                    int end = 50;
                    Edge edgeA = new Edge(Edge.Type.POS, start);

                    signal.Anchor(Signal.Type.HIGH, start);
                    Edge[] expectedAfterCreate = {leftBound, edgeA, rightBound};
                    Edge[] actualAfterCreate = signal.GetEdges().toArray(new Edge[0]);
                    assertArrayEquals(expectedAfterCreate, actualAfterCreate);
                }

                @DisplayName("Flip")
                @Test
                void testFlip() {
                    int start = 100;
                    Edge edgeA = new Edge(Edge.Type.NEG, start);

                    signal.Anchor(Signal.Type.HIGH, start);
                    signal.SetDirectionLeft();

                    Edge[] expectedAfterFlip = {leftBound, edgeA, rightBound};
                    Edge[] actualAfterFlip = signal.GetEdges().toArray(new Edge[0]);
                    assertArrayEquals(expectedAfterFlip, actualAfterFlip);

                }

                @Nested
                @DisplayName("Dragging")
                class testDrag {
                    int start;
                    int end;
                    Edge edgeA;
                    Edge edgeB;

                    @BeforeEach
                    void createHighSignal() {
                        start = 100;
                        end = 50;
                        edgeA = new Edge(Edge.Type.NEG, start);
                        edgeB = new Edge(Edge.Type.POS, end);

                        signal.Anchor(Signal.Type.HIGH, start);

                        signal.SetDirectionLeft();
                        // Simulates dragging
                        for (int i = start; i >= end; i--) {
                            signal.Extend(i);
                        }
                    }

                    @DisplayName("Left drag create")
                    @Test
                    void testSignalCreation() {
                        // Test for edges:
                        Edge[] expectedAfterInsert = {leftBound, edgeB, edgeA, rightBound};
                        Edge[] actualAfterInsert = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expectedAfterInsert, actualAfterInsert);
                    }

                    @DisplayName("Extend right")
                    void extendRight() {
                        // Drag again
                        int newStart = 90;
                        int newEnd = 150;

                        signal.Anchor(Signal.Type.HIGH, newStart);
                        signal.SetDirectionLeft();
                        for (int i = newStart; i >= newEnd; i--) {
                            signal.Extend(i);
                        }

                        edgeA.SetCoord(newEnd);

                        // Test for edges:
                        Edge[] expectedAfterExtend = {leftBound, edgeB, edgeA, rightBound};
                        Edge[] actualAfterExtend = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expectedAfterExtend, actualAfterExtend);
                    }

                    @DisplayName("Extend left")
                    @Test
                    void extendLeft() {
                        // Drag again
                        int newStart = 60;
                        int newEnd = 10;

                        signal.Anchor(Signal.Type.HIGH, newStart);
                        signal.SetDirectionLeft();
                        for (int i = newStart; i >= newEnd; i--) {
                            signal.Extend(i);
                        }

                        edgeB.SetCoord(newEnd);

                        // Test for edges:
                        Edge[] expectedAfterExtend = {leftBound, edgeB, edgeA, rightBound};
                        Edge[] actualAfterExtend = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expectedAfterExtend, actualAfterExtend);
                    }

                    @DisplayName("Extend left then right")
                    @Test
                    void extendBoth() {
                        signal.PrintEdges();
                        int newStart = 90;
                        int newEnd = 150;
                        int newMid = 10;

                        // drag left
                        signal.Anchor(Signal.Type.HIGH, newStart);
                        signal.SetDirectionLeft();
                        for (int i = newStart; i >= newMid; i--) {
                            signal.Extend(i);
                        }

                        signal.PrintEdges();

                        // drag right
                        signal.Anchor(Signal.Type.HIGH, newMid);
//                        signal.Flip();
                        for (int i = newMid; i <= newEnd; i++) {
                            signal.Extend(i);
                        }

                        signal.PrintEdges();

                        edgeA.SetCoord(newMid);
                        edgeA.SetType(Edge.Type.POS);
                        edgeB.SetCoord(newEnd);
                        edgeB.SetType(Edge.Type.NEG);

                        Edge[] expected = {leftBound, edgeA, edgeB, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                        assertArrayEquals(expected, actual);
                    }

                    @DisplayName("Delete right")
                    @Test
                    void deleteRight() {
                        // Simulate a right-click drag over the existing signal
                        start = 30;
                        end = 120;

                        signal.Anchor(Signal.Type.LOW, start);
                        for (int i = start; i <= end; i++) {
                            signal.Extend(i);
                        }

                        Edge[] expected = {leftBound, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expected, actual);
                    }

                    @DisplayName("Delete left")
                    @Test
                    void deleteLeft() {
                        // Simulate a right-click drag over the existing signal
                        start = 120;
                        end = 30;

                        signal.Anchor(Signal.Type.LOW, start);
                        signal.SetDirectionLeft();
                        for (int i = start; i >= end; i--) {
                            signal.Extend(i);
                        }

                        Edge[] expected = {leftBound, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expected, actual);
                    }
                }
            }
        }

        @Nested
        @DisplayName("Two signals")
        class TwoSignals {
            int start1;
            int end1;

            int start2;
            int end2;

            @BeforeAll
            void initSignals() {

            }

            @Test
            @DisplayName("Merge high signals")
            void mergeHighSignals() {

            }
        }
    }
}