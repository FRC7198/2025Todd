import static org.junit.jupiter.api.Assertions.assertEquals;

import frc.robot.Constants;
import frc.robot.subsystems.Elevatorsubsystem;
import org.junit.jupiter.api.Test;

public class ElevatorSubsystemTests {

    private Elevatorsubsystem elevatorsubsystem;

    public ElevatorSubsystemTests() {
        elevatorsubsystem = new Elevatorsubsystem();
    }

    @Test
    public void SpeedTest() {

        elevatorsubsystem.setTargetPosition(Constants.ElevatorConstants.ELEVATOR_BOTTOM_POSITION);

        double speed = elevatorsubsystem.calculateElevatorMotorSpeed(0);
        assertEquals(0, speed);

        
    }
}
