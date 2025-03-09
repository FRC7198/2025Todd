package frc.robot.subsystems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import edu.wpi.first.hal.HAL;
import frc.robot.Constants.FlipperConstants;
import frc.robot.subsystems.FlipperSubsystem.FlipperState;

public class FlipperSubsystemTests {

    Logger logger = Logger.getLogger(FlipperSubsystemTests.class.getName());
    private FlipperSubsystem flipperSubsystem;

    public FlipperSubsystemTests() {

    }

        @BeforeEach // this method will run before each test
    void setup() {
      assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
      flipperSubsystem = new FlipperSubsystem();
    }

    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @AfterEach // this method will run after each test
    void shutdown() throws Exception {
      flipperSubsystem.close(); // destroy our intake object
    }
  

    @ParameterizedTest
    @MethodSource("flipperTestCases")
    public void flipperTest(double encoderPosition,FlipperState initialFlipperState,boolean initialIsInFlipState, double expectedSpeed, FlipperState expectedflipperState, boolean expectedIsInFlipCycle) {

        //Arrange - setup
        flipperSubsystem.setFlipperState(initialFlipperState);
        if(initialIsInFlipState) {
            flipperSubsystem.RunFlipCycle();
        }
        
        //ACT
        double flipperMotorSpeed = flipperSubsystem.flip(encoderPosition);

        logger.log(Level.FINE ,"encoderPosition: " + encoderPosition + 
        " speed: " + flipperMotorSpeed + " expected speed: " + expectedSpeed +
        " expectedFlipperState: " + expectedflipperState + " FlipperState: " + flipperSubsystem.getFlipperState() + 
        " expectedShouldBeFlipping: " + expectedIsInFlipCycle + " shouldBeFlipping: " + flipperSubsystem.getIsInFlipCycle());

        //ASSERT
        assertEquals(expectedSpeed, flipperMotorSpeed);
        assertEquals(expectedflipperState, flipperSubsystem.getFlipperState());
        assertEquals(expectedIsInFlipCycle, flipperSubsystem.getIsInFlipCycle());

    }

    private static Stream<Arguments> flipperTestCases() {
        return Stream.of(
                // Intitial just turned on state should not move the flipper
                Arguments.of(0, FlipperState.RESET, false, 0.0, FlipperState.RESET, false),
                // Initial state of button press so that flipping should begin
                Arguments.of(0, FlipperState.RESET, true, FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue(), FlipperState.FLIPPING, true),
                // Mid Flip Cycle as Encoder Advances
                Arguments.of(.75, FlipperState.FLIPPING, true, FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue(), FlipperState.FLIPPING, true),
                Arguments.of(1, FlipperState.FLIPPING, true, FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue(), FlipperState.FLIPPING, true),
                // We've hit the end of the flip start returning to the original position
                Arguments.of(FlipperConstants.FLIPPER_TILT_POSITION, FlipperState.FLIPPING, true, FlipperConstants.FLIPPER_MOTOR_BACK_SPEED.doubleValue(), FlipperState.RETURNING, true),
                // We are mid returning flipper should continue to reset
                Arguments.of(1, FlipperState.RETURNING, true, FlipperConstants.FLIPPER_MOTOR_BACK_SPEED.doubleValue(), FlipperState.RETURNING, true),
                Arguments.of(.5, FlipperState.RETURNING, true, FlipperConstants.FLIPPER_MOTOR_BACK_SPEED.doubleValue(), FlipperState.RETURNING, true),
                // We've returned to home position we should be back to reset
                Arguments.of(FlipperConstants.FLIPPER_STARTING_POSITION, FlipperState.RETURNING, true, 0.0, FlipperState.RESET, false)
                );
    }
    
}
