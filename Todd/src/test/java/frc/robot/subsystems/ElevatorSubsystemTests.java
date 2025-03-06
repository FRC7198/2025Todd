import static org.junit.jupiter.api.Assertions.assertEquals;

import frc.robot.Constants;
import frc.robot.subsystems.Elevatorsubsystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import java.util.stream.*;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.logging.Logger;
import java.util.logging.Level;
import edu.wpi.first.hal.HAL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

public class ElevatorSubsystemTests {

    Logger logger = Logger.getLogger(ElevatorSubsystemTests.class.getName());
    private Elevatorsubsystem elevatorsubsystem;

    public ElevatorSubsystemTests() {
    }

    @BeforeEach // this method will run before each test
    void setup() {
      assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
      elevatorsubsystem = new Elevatorsubsystem();
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @AfterEach // this method will run after each test
    void shutdown() throws Exception {
      elevatorsubsystem.close(); // destroy our intake object
    }
  
    @ParameterizedTest
    @MethodSource("speedCalculationsTestCases")
    public void SpeedTest(double encoderPosition, double target, double expectedSpeed) {
        elevatorsubsystem.setTargetPosition(target);
        double speed = elevatorsubsystem.calculateElevatorMotorSpeed(encoderPosition);
        logger.log(Level.INFO,String.format("Speed is {0} but expected {1}", speed, expectedSpeed));
        assertEquals(expectedSpeed, speed);
    }

    private static Stream<Arguments> speedCalculationsTestCases() {
        return Stream.of(
                Arguments.of(0, Constants.ElevatorConstants.ELEVATOR_BOTTOM_POSITION, 0.0),
                Arguments.of(-50, Constants.ElevatorConstants.ELEVATOR_BOTTOM_POSITION, .1)
                );
    }
}
