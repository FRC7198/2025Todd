package   frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;

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
        double speed = elevatorsubsystem.calculateElevatorMotorSpeed(encoderPosition, false, false);
        logger.log(Level.INFO,"Speed is " + speed + " and expect " + expectedSpeed);
        assertEquals(expectedSpeed, speed);
    }

    private static Stream<Arguments> speedCalculationsTestCases() {
        return Stream.of(
                //we are at the bottom and need to move to the bottom then don't move
                Arguments.of(Constants.ElevatorConstants.ELEVATOR_BOTTOM_POSITION, Constants.ElevatorConstants.ELEVATOR_BOTTOM_POSITION, 0.0),
                // we are in the middle and need to the bottom so move down (positive motor speed)
                Arguments.of(Constants.ElevatorConstants.ELEVATOR_L2, Constants.ElevatorConstants.ELEVATOR_BOTTOM_POSITION, ElevatorConstants.ELEVATOR_MOTOR_SPEED_DOWN),
                // we are in the middle and need to the bottom so move to the l1 (positive motor speed)
                Arguments.of(Constants.ElevatorConstants.ELEVATOR_L2, Constants.ElevatorConstants.ELEVATOR_L1, ElevatorConstants.ELEVATOR_MOTOR_SPEED_DOWN),
                // we are at the bottom and need to move up to the first rung so move up (negative motor speed)
                Arguments.of(Constants.ElevatorConstants.ELEVATOR_BOTTOM_POSITION, Constants.ElevatorConstants.ELEVATOR_L1, ElevatorConstants.ELEVATOR_MOTOR_SPEED_UP),
                // we are at the first postion and need to move up to the highest run so move up (negative motor speed)
                Arguments.of(Constants.ElevatorConstants.ELEVATOR_L1, Constants.ElevatorConstants.ELEVATOR_L3, ElevatorConstants.ELEVATOR_MOTOR_SPEED_UP),
                // we are at close to the l3 position so don't move
                Arguments.of(Constants.ElevatorConstants.ELEVATOR_L3, Constants.ElevatorConstants.ELEVATOR_L3, 0)
                );
    }
}
