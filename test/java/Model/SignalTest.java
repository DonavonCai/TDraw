package Model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

class SignalTest {
    // These members are used for all tests in this suite.
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
    @DisplayName("Bounds")
    class testBounds {
        @Test
        @DisplayName("Drag past left bound")
        void testLeft() {
            int start = leftBound.GetCoord() + 10;
            int end = leftBound.GetCoord() - 10;

            signal.Anchor(Signal.Type.LOW, start);

            signal.SetDirectionLeft();
            for (int i = start; i >= end; i--) {
                signal.Extend(i);
            }

            Edge[] expected = {leftBound, rightBound};
            Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
            assertArrayEquals(expected, actual);
        }

        @Test
        @DisplayName("Drag past right bound")
        void testRight() {
            int start = rightBound.GetCoord() - 10;
            int end = rightBound.GetCoord() + 10;

            signal.Anchor(Signal.Type.LOW, start);

            for (int i = start; i <= end; i++) {
                signal.Extend(i);
            }

            Edge[] expected = {leftBound, rightBound};
            Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
            assertArrayEquals(expected, actual);
        }

        @Test
        @DisplayName("Drag past both")
        void testBoth() {
            assertEquals(0, 1);
        }
    }

    @Nested
    @DisplayName("Creating high signals")
    class highSignal {
        @Nested
        @DisplayName("One Signal")
        class oneSignal {
            @Nested
            @DisplayName("Create by dragging right")
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
                @DisplayName("Drag on existing:")
                class testDrag {
                    int pointA = 50;
                    int pointB = 100;
                    Edge edgeA;
                    Edge edgeB;

                    @BeforeEach
                    void createHighSignal() {
                        // Record the edges for testing
                        edgeA = new Edge(Edge.Type.POS, pointA);
                        edgeB = new Edge(Edge.Type.NEG, pointB);

                        signal.Anchor(Signal.Type.HIGH, pointA);
                        // Simulates dragging
                        for (int i = pointA; i <= pointB; i++) {
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
                        int newStart = pointB - 10;
                        int newEnd = pointB + 60;
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
                        int newStart = pointA + 10;
                        int newEnd = leftBound.GetCoord() + 10;
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
                        int newStart = pointB - 10;
                        int newMid = pointB + 50;
                        int newEnd = leftBound.GetCoord() + 10;

                        signal.Anchor(Signal.Type.HIGH, newStart);
                        for (int i = newStart; i <= newMid; i++) {
                            signal.Extend(i);
                        }

                        signal.Anchor(Signal.Type.HIGH, newMid);
                        signal.SetDirectionLeft();
                        for (int i = newMid; i >= newEnd; i--) {
                            signal.Extend(i);
                        }

                        edgeA.SetCoord(newEnd);
                        edgeA.SetType(Edge.Type.POS);
                        edgeB.SetCoord(newMid);
                        edgeB.SetType(Edge.Type.NEG);

                        Edge[] expected = {leftBound, edgeA, edgeB, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                        assertArrayEquals(expected, actual);
                    }

                    @Test
                    @DisplayName("Shorten left side")
                    void shortenLeft() {
                        int start = pointA - 10;
                        int end = pointB - 10;

                        signal.Anchor(Signal.Type.LOW, start);
                        for (int i = start; i <= end; i++) {
                            signal.Extend(i);
                        }

                        edgeA.SetCoord(end);
                        Edge[] expected = {leftBound, edgeA, edgeB, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                        assertArrayEquals(expected, actual);
                    }

                    @Test
                    @DisplayName("Shorten right side")
                    void shortenRight() {
                        int start = pointB + 10;
                        int end = pointA + 10;

                        signal.Anchor(Signal.Type.LOW, start);
                        signal.SetDirectionLeft();
                        for (int i = start; i >= end; i--) {
                            signal.Extend(i);
                        }

                        edgeB.SetCoord(end);
                        Edge[] expected = {leftBound, edgeA, edgeB, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                        assertArrayEquals(expected, actual);
                    }

                    @DisplayName("Delete right")
                    @Test
                    void deleteRight() {
                        // Simulate a right-click drag over the existing signal
                        pointA = 30;
                        pointB = 120;

                        signal.Anchor(Signal.Type.LOW, pointA);
                        for (int i = pointA; i <= pointB; i++) {
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
                        pointA = 120;
                        pointB = 30;

                        signal.Anchor(Signal.Type.LOW, pointA);
                        signal.SetDirectionLeft();
                        for (int i = pointA; i >= pointB; i--) {
                            signal.Extend(i);
                        }

                        Edge[] expected = {leftBound, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
                        assertArrayEquals(expected, actual);
                    }
                }
            }

            @Nested
            @DisplayName("Create by dragging left")
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
                @DisplayName("Drag on existing:")
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
                        int newStart = 90;
                        int newEnd = 150;
                        int newMid = 10;

                        // drag left
                        signal.Anchor(Signal.Type.HIGH, newStart);
                        signal.SetDirectionLeft();
                        for (int i = newStart; i >= newMid; i--) {
                            signal.Extend(i);
                        }

                        // drag right
                        signal.Anchor(Signal.Type.HIGH, newMid);
                        for (int i = newMid; i <= newEnd; i++) {
                            signal.Extend(i);
                        }

                        edgeA.SetCoord(newMid);
                        edgeA.SetType(Edge.Type.POS);
                        edgeB.SetCoord(newEnd);
                        edgeB.SetType(Edge.Type.NEG);

                        Edge[] expected = {leftBound, edgeA, edgeB, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                        assertArrayEquals(expected, actual);
                    }

                    @Test
                    @DisplayName("Shorten left side")
                    void shortenLeft() {
                        int start = edgeB.GetCoord() - 10;
                        int end = edgeA.GetCoord() - 10;

                        signal.PrintEdges();

                        signal.Anchor(Signal.Type.LOW, start);
                        for (int i = start; i <= end; i++) {
                            signal.Extend(i);
                        }

                        signal.PrintEdges();
                        edgeB.SetCoord(end);
                        Edge[] expected = {leftBound, edgeB, edgeA, rightBound};
                        Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                        assertArrayEquals(expected, actual);
                    }

                    @Test
                    @DisplayName("Shorten right side")
                    void shortenRight() {
                        int start = edgeA.GetCoord() + 10;
                        int end = edgeB.GetCoord() + 10;

                        signal.Anchor(Signal.Type.LOW, start);
                        signal.SetDirectionLeft();
                        for (int i = start; i >= end; i--) {
                            signal.Extend(i);
                        }

                        edgeA.SetCoord(end);
                        Edge[] expected = {leftBound, edgeB, edgeA, rightBound};
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
        @DisplayName("Two Signals")
        class twoSignals {
            int pointA = 50;
            int pointB = 100;
            Edge edgeA;
            Edge edgeB;

            int pointC = 150;
            int pointD = 200;
            Edge edgeC;
            Edge edgeD;

            @BeforeEach
            void initSignals() {
                edgeA = new Edge(Edge.Type.POS, pointA);
                edgeB = new Edge(Edge.Type.NEG, pointB);
                edgeC = new Edge(Edge.Type.POS, pointC);
                edgeD = new Edge(Edge.Type.NEG, pointD);

                signal.Anchor(Signal.Type.HIGH, pointA);
                for (int i = pointA; i <= pointB; i++) {
                    signal.Extend(i);
                }

                signal.Anchor(Signal.Type.HIGH, pointC);
                for (int i = pointC; i <= pointD; i++) {
                    signal.Extend(i);
                }
            }

            @Test
            @DisplayName("Signals created properly")
            void testCreation() {
                Edge[] expected = {leftBound, edgeA, edgeB, edgeC, edgeD, rightBound};
                Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                assertArrayEquals(expected, actual);
            }

            @Test
            @DisplayName("Merge right")
            void mergeRight() {
                int start = pointB - 10;
                int end = pointC + 10;

                signal.Anchor(Signal.Type.HIGH, start);
                for (int i = start; i <= end; i++) {
                    signal.Extend(i);
                }

                Edge[] expected = {leftBound, edgeA, edgeD, rightBound};
                Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                assertArrayEquals(expected, actual);
            }

            @Test
            @DisplayName("Merge left")
            void mergeLeft() {
                int start = pointC + 10;
                int end = pointB - 10;

                signal.Anchor(Signal.Type.HIGH, start);
                signal.SetDirectionLeft();
                for (int i = start; i >= end; i--) {
                    signal.Extend(i);
                }

                Edge[] expected = {leftBound, edgeA, edgeD, rightBound};
                Edge[] actual = signal.GetEdges().toArray(new Edge[0]);

                assertArrayEquals(expected, actual);
            }

            @Test
            @DisplayName("Clear right")
            void clearRight() {
                int start = pointA - 10;
                int end =  pointD + 10;

                signal.Anchor(Signal.Type.LOW, start);
                for (int i = start; i <= end; i++) {
                    signal.Extend(i);
                }

                Edge[] expected = {leftBound, rightBound};
                Edge[] actual = signal.GetEdges().toArray(new Edge[0]);
                assertArrayEquals(expected, actual);
            }

            @Test
            @DisplayName("Clear left")
            void clearLeft() {
                int start = pointD + 10;
                int end =  pointA - 10;

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