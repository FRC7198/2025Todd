package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ElevatorConstants;

public class Elevatorsubsystem extends SubsystemBase {

    public enum ElevatorState {
        RAISING,
        IDLE,
        DESCENDING
    }

    public ElevatorState currentElevatorState = ElevatorState.IDLE;

    private SparkMax elevationMotor;
    private SparkMaxConfig elevationMotorConfig;
    private SparkClosedLoopController closedLoopController;
    private AbsoluteEncoder encoder;

    public Elevatorsubsystem() {

        elevationMotor = new SparkMax(ElevatorConstants.ELEVATION_MOTOR_ID, MotorType.kBrushless);
        closedLoopController = elevationMotor.getClosedLoopController();
        elevationMotorConfig = new SparkMaxConfig();
        encoder = elevationMotor.getAbsoluteEncoder();
        currentElevatorState = ElevatorState.IDLE;

         /*
        * Configure the encoder. For this specific example, we are using the
        * integrated encoder of the NEO, and we don't need to configure it. If
        * needed, we can adjust values like the position or velocity conversion
        * factors.
        */
        elevationMotorConfig.encoder
        .positionConversionFactor(1)
        .velocityConversionFactor(1);

        /*
        * Configure the closed loop controller. We want to make sure we set the
        * feedback sensor as the primary encoder.
        */
        elevationMotorConfig.closedLoop
            .feedbackSensor(FeedbackSensor.kPrimaryEncoder)
            // Set PID values for position control. We don't need to pass a closed loop
            // slot, as it will default to slot 0.
            .p(0.1)
            .i(0)
            .d(0)
            .outputRange(-1, 1)
            // Set PID values for velocity control in slot 1
            .p(0.0001, ClosedLoopSlot.kSlot1)
            .i(0, ClosedLoopSlot.kSlot1)
            .d(0, ClosedLoopSlot.kSlot1)
            .velocityFF(1.0 / 5767, ClosedLoopSlot.kSlot1)
            .outputRange(-1, 1, ClosedLoopSlot.kSlot1);

        /*
        * Apply the configuration to the SPARK MAX.
        *
        * kResetSafeParameters is used to get the SPARK MAX to a known state. This
        * is useful in case the SPARK MAX is replaced.
        *
        * kPersistParameters is used to ensure the configuration is not lost when
        * the SPARK MAX loses power. This is useful for power cycles that may occur
        * mid-operation.
        */
        elevationMotor.configure(elevationMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kNoPersistParameters);

        // Initialize dashboard values
        SmartDashboard.setDefaultNumber("Target Position", 0);
        SmartDashboard.setDefaultNumber("Target Velocity", 0);
        SmartDashboard.setDefaultBoolean("Control Mode", false);
        SmartDashboard.setDefaultBoolean("Reset Encoder", false);
    }

    public boolean RaiseElevator() {

        currentElevatorState = ElevatorState.RAISING;
        return true;
    }

    public boolean LowerElevator() {

        currentElevatorState = ElevatorState.DESCENDING;
        return true;
    }

}
