package Model.Helper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import Model.Signal;

class SignalTreeTest {
    @Nested
    @DisplayName("One high signal")
    class OneHighSignal {
        @DisplayName("50 -> 100: Right to left")
        @Test
        void drawOneSignal() {
            int leftBound = 0;
            int rightBound = 1000;

            Signal root = new Signal();
            root.SetCoords(leftBound, rightBound);

            SignalNode rootNode = new SignalNode(root);
            SignalTree test = new SignalTree(rootNode);

            int start = 50;
            int end = 100;
            test.AddSignal(start);
            test.ExtendSignal(end);
            test.FinishSignal();

            // todo: expected?
        }

        @DisplayName("100 -> 50: Left to right")
        @Test
        void drawHighSignalBackwards() {
            int leftBound = 0;
            int rightBound = 1000;

            Signal root = new Signal();
            root.SetCoords(leftBound, rightBound);

            SignalNode rootNode = new SignalNode(root);
            SignalTree test = new SignalTree(rootNode);

            int start = 100;
            int end = 50;
            test.AddSignal(start);
            test.ExtendSignal(end);
            test.FinishSignal();

            // todo: expected?
        }

        @DisplayName("50 -> 100 -> 75: Left / Right / Left")
        @Test
        void leftRightLeft() {
            SignalTree test = new SignalTree();

            int start = 50;
            int middle = 100;
            int end = 75;
            test.AddSignal(start);
            test.ExtendSignal(middle);
            test.ExtendSignal(end);
            test.FinishSignal();

            // todo: expected
        }

        @DisplayName("50- -> 100 -> 0: Flip signal towards left")
        @Test
        void flipSignalTowardsLeft() {
            SignalTree test = new SignalTree();

            int start = 50;
            int middle = 100;
            int end = 0;
            test.AddSignal(start);
            test.ExtendSignal(middle);
            test.ExtendSignal(end);
            test.FinishSignal();

            // todo: expected
        }
    }

    @Nested
    @DisplayName("Multiple high signals")
    class TwoHighSignals {
        @DisplayName("2 high signals")
        @Test
        void twoHighSignals() {
            SignalTree test = new SignalTree();

            int start1 = 25;
            int end1 = 50;
            int start2 = 75;
            int end2 = 100;

            test.AddSignal(start1);
            test.ExtendSignal(end1);
            test.FinishSignal();

            test.AddSignal(start2);
            test.ExtendSignal(end2);
            test.FinishSignal();

            // todo: expected
        }
    }
}