package Model.Helper;

import Model.Signal;
import Model.Edge;
//import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignalNodeTest {
    @DisplayName("Constructor")
    @Test
    void create1Signal() {
        Signal s = new Signal();
        SignalNode test = new SignalNode(s);

        assertEquals(s, test.GetSignal());
    }

    @Nested
    @DisplayName("Set Type")
    class Type {
        @DisplayName("Low signal")
        @Test
        void lowSignal() {
            Signal s = new Signal(0, 1000, Signal.Type.LOW);
            SignalNode test = new SignalNode(s);

            assertEquals(Edge.Type.NEG, s.GetLeftEdge().GetType());
            assertEquals(Edge.Type.POS, s.GetRightEdge().GetType());
        }

        @DisplayName("High signal")
        @Test
        void highSignal() {
            Signal s = new Signal(0, 1000, Signal.Type.HIGH);
            SignalNode test = new SignalNode(s);

            assertEquals(Edge.Type.POS, s.GetLeftEdge().GetType());
            assertEquals(Edge.Type.NEG, s.GetRightEdge().GetType());
        }
    }

    @Nested
    @DisplayName("Tree Traversal")
    class TreeTraversal {
        @Nested
        @DisplayName("Two levels")
        class TwoLevels {
            @Nested
            @DisplayName("In order: (25-50, high); (50-75, low); (75-100, high);")
            class InOrder {
                Signal root;
                Signal a;
                Signal b;
                Signal c;

                SignalNode rootNode;
                SignalNode nodeA;
                SignalNode nodeB;
                SignalNode nodeC;

                @BeforeEach
                void initTest() {
                    root = new Signal(0, 1000, Signal.Type.LOW);
                    a = new Signal(25, 50, Signal.Type.HIGH);
                    b = new Signal(50, 75, Signal.Type.LOW);
                    c = new Signal (75, 100, Signal.Type.HIGH);

                    rootNode = new SignalNode(root);
                    nodeA = new SignalNode(a);
                    nodeB = new SignalNode(b);
                    nodeC = new SignalNode(c);

                    rootNode.Insert(nodeA);
                    rootNode.Insert(nodeB);
                    rootNode.Insert(nodeC);
                }

                @DisplayName("Coords in order")
                @Test
                void testCoords() {
                    Integer[] expected = {0, 25, 50, 50, 75, 75, 100, 1000};
                    Integer[] actual = rootNode.CoordsInOrder().toArray(new Integer[0]);

                    assertArrayEquals(expected, actual);
                }

                @DisplayName("Node positions")
                @Test
                void testNodes() {
                    SignalNode[] root_expected = {nodeA, nodeB, nodeC};
                    SignalNode[] root_actual = rootNode.GetChildren().toArray(new SignalNode[0]);

                    assertArrayEquals(root_expected, root_actual);
                }
            }

            @Nested
            @DisplayName("Out of order: (75-100, high); (50-75, low); (75-100, high);")
            class OutOfOrder {
                @DisplayName("Coords")
                @Test
                void testCoords() {
                    Signal s = new Signal(0, 1000, Signal.Type.LOW);
                    Signal s2 = new Signal(25, 50, Signal.Type.HIGH);
                    Signal s3 = new Signal(50, 75, Signal.Type.LOW);
                    Signal s4 = new Signal (75, 100, Signal.Type.HIGH);

                    SignalNode test = new SignalNode(s);
                    test.Insert(new SignalNode(s4));
                    test.Insert(new SignalNode(s2));
                    test.Insert(new SignalNode(s3));

                    Integer[] expected = {0, 25, 50, 50, 75, 75, 100, 1000};
                    Integer[] actual = test.CoordsInOrder().toArray(new Integer[0]);

                    assertArrayEquals(expected, actual);
                }

                @DisplayName("Node positions")
                @Test
                void testNodes() {
                    Signal root = new Signal(0, 1000, Signal.Type.LOW);
                    Signal s2 = new Signal(25, 50, Signal.Type.HIGH);
                    Signal s3 = new Signal(50, 75, Signal.Type.LOW);
                    Signal s4 = new Signal (75, 100, Signal.Type.HIGH);

                    SignalNode s2Node = new SignalNode(s2);
                    SignalNode s3Node = new SignalNode(s3);
                    SignalNode s4Node = new SignalNode(s4);

                    SignalNode test = new SignalNode(root);
                    test.Insert(s4Node);
                    test.Insert(s2Node);
                    test.Insert(s3Node);

                    SignalNode[] root_expected = {s2Node, s3Node, s4Node};
                    SignalNode[] root_actual = test.GetChildren().toArray(new SignalNode[0]);
                    assertArrayEquals(root_expected, root_actual);
                }
            }
        }

        @Nested
        @DisplayName("Four levels")
        class FourLevels {
            @Nested
            @DisplayName("Single child")
            class SingleChild {
                @DisplayName("One branch")
                @Test
                void oneBranch() {
                    Signal root = new Signal(0, 1000, Signal.Type.LOW);
                    Signal s1 = new Signal(100, 900, Signal.Type.HIGH);
                    Signal s2 = new Signal(200, 800, Signal.Type.LOW);
                    Signal s3 = new Signal(300, 700, Signal.Type.HIGH);

                    SignalNode rootNode = new SignalNode(root);
                    SignalNode levelTwoNode = new SignalNode(s1);
                    SignalNode levelThreeNode = new SignalNode(s2);
                    SignalNode levelFourNode = new SignalNode(s3);

                    rootNode.Insert(levelTwoNode);
                    rootNode.Insert(levelThreeNode);
                    rootNode.Insert(levelFourNode);

                    SignalNode[] root_expected = {levelTwoNode};
                    SignalNode[] root_actual = rootNode.GetChildren().toArray(new SignalNode[0]);
                    assertArrayEquals(root_expected, root_actual);

                    SignalNode test_levelTwo = rootNode.GetChildren().get(0);
                    SignalNode[] levelTwo_expected = {levelThreeNode};
                    SignalNode[] levelTwo_actual = test_levelTwo.GetChildren().toArray(new SignalNode[0]);
                    assertArrayEquals(levelTwo_expected, levelTwo_actual);

                    SignalNode test_levelThree = test_levelTwo.GetChildren().get(0);
                    SignalNode[] levelThree_expected = {levelFourNode};
                    SignalNode[] levelThree_actual = test_levelThree.GetChildren().toArray(new SignalNode[0]);
                    assertArrayEquals(levelThree_expected, levelThree_actual);

                    SignalNode test_levelFour = test_levelThree.GetChildren().get(0);
                    SignalNode[] levelFour_expected = {};
                    SignalNode[] levelFour_actual = test_levelFour.GetChildren().toArray(new SignalNode[0]);
                    assertArrayEquals(levelFour_expected, levelFour_actual);
                }

                @DisplayName("Two branches")
                @Test
                void twoBranches() {
                    /*   root
                     *   /  \
                     *  A    B
                     *  |    |
                     *  C    D
                     * */
                    Signal root = new Signal(0, 1000, Signal.Type.LOW);
                    Signal a = new Signal(100, 400, Signal.Type.HIGH);
                    Signal b = new Signal(600, 900, Signal.Type.LOW);
                    Signal c = new Signal(200, 300, Signal.Type.LOW);
                    Signal d = new Signal(700, 800, Signal.Type.HIGH);

                    SignalNode test_root = new SignalNode(root);
                    SignalNode nodeA = new SignalNode(a);
                    SignalNode nodeB = new SignalNode(b);
                    SignalNode nodeC = new SignalNode(c);
                    SignalNode nodeD = new SignalNode(d);

                    test_root.Insert(nodeA);
                    test_root.Insert(nodeB);
                    test_root.Insert(nodeC);
                    test_root.Insert(nodeD);

                    SignalNode[] root_expected = {nodeA, nodeB};
                    SignalNode[] root_actual = test_root.GetChildren().toArray(new SignalNode[0]);
                    assertArrayEquals(root_expected, root_actual);

                    // Left branch:
                    SignalNode test_nodeA = test_root.GetChildren().get(0);
                    SignalNode[] nodeA_expected = {nodeC};
                    SignalNode[] nodeA_actual = test_nodeA.GetChildren().toArray(new SignalNode[0]);
                    assertArrayEquals(nodeA_expected, nodeA_actual);

                    SignalNode test_nodeC = test_nodeA.GetChildren().get(0);
                    assertTrue(test_nodeC.GetChildren().isEmpty());

                    // Right branch:
                    SignalNode test_nodeB = test_root.GetChildren().get(1);
                    SignalNode[] nodeB_expected = {nodeD};
                    SignalNode[] nodeB_actual = test_nodeB.GetChildren().toArray(new SignalNode[0]);
                    assertArrayEquals(nodeB_expected, nodeB_actual);

                    SignalNode test_nodeD = test_nodeB.GetChildren().get(0);
                    assertTrue(test_nodeD.GetChildren().isEmpty());
                }
            }
        }
    }

    @Nested
    @DisplayName("Dragging")
    class Dragging {
        @Nested
        @DisplayName("Extension")
        class Extension {
            @Nested
            @DisplayName("Single Signal")
            class Single {
                @Test
                @DisplayName("High signal")
                void testOne() {
                    Signal root = new Signal(0, 1000, Signal.Type.LOW);
                    Signal a = new Signal(25, 50, Signal.Type.HIGH);

                    SignalNode rootNode = new SignalNode(root);
                    rootNode.Insert(new SignalNode(a));

                    Integer[] expected = {0, 25, 50, 1000};
                    Integer[] actual = rootNode.CoordsInOrder().toArray(new Integer[0]);

                    // todo: extend
                }
            }
        }
    }
}