package timingdiagram;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        DSignalTest.class,
        MousePressHandlerTest.class
//        MouseDragHandlerTest.class,
//        MouseReleaseHandlerTest.class
})

public class UnitTestSuite {

}
