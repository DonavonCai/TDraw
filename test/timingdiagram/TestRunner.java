package timingdiagram;

import javafx.embed.swing.JFXPanel;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        System.out.println("initializing toolkit");
        new JFXPanel();

        Result result = JUnitCore.runClasses(UnitTestSuite.class);
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
            failure.getException().printStackTrace();
        }

        System.out.println("Unit tests successful? " + result.wasSuccessful());
        System.exit(0);
    }
}
