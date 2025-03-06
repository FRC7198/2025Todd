package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.ClosedLoopSlot;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.ClosedLoopConfig.FeedbackSensor;
import com.revrobotics.spark.config.SparkMaxConfig;
import java.math.BigDecimal;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;

public class Elevatorsubsystem extends SubsystemBase implements AutoCloseable {

    public enum ElevatorState {
        RAISING,
        IDLE,
        DESCENDING
    }

    public ElevatorState currentElevatorState = ElevatorState.IDLE;

    private SparkMax elevatorMotor;
    // private DigitalInput topLimitSwitch = new DigitalInput(0);
    private DigitalInput bottomLimitSwitch = new DigitalInput(0);
    private double targetPosition;
    private RelativeEncoder elevatorEncoder;

    public Elevatorsubsystem() {

        elevatorMotor = new SparkMax(ElevatorConstants.ELEVATION_MOTOR_ID, MotorType.kBrushless);
        elevatorEncoder = elevatorMotor.getEncoder();
        currentElevatorState = ElevatorState.IDLE;

        // Initialize dashboard values
        SmartDashboard.setDefaultNumber("Target Position", 0);
        SmartDashboard.setDefaultNumber("Target Velocity", 0);
        SmartDashboard.setDefaultBoolean("Control Mode", false);
        SmartDashboard.setDefaultBoolean("Reset Encoder", false);
    }

    @Override
    public void periodic() {

        Double speed = calculateElevatorMotorSpeed(elevatorEncoder.getPosition());
        if (speed == 0) {
            stop();
        } else {
            elevatorMotor.set(speed); // Adjust speed as needed
        }

        SmartDashboard.putNumber("elevatorspeed", speed);
        SmartDashboard.putNumber("targetposition", targetPosition);
        SmartDashboard.putNumber("elevatorencoderpos", elevatorEncoder.getPosition());
        SmartDashboard.putBoolean("isattop", isAtTop());
        SmartDashboard.putBoolean("isatbottom", isAtBottom());

    }

    public double calculateElevatorMotorSpeed(double encoderPosition) { 
        
        // Stop motor if limit switch is triggered
        BigDecimal speed = new BigDecimal(0.0);
        if (isAtTop()) {
            return 0;
        } else if (isAtBottom()) {
            return 0;
        } else {
            // Move towards target position
            double workingPosition = targetPosition - encoderPosition;
            SmartDashboard.putNumber("workingPosition", workingPosition);
            if (workingPosition < encoderPosition)
            {
                speed = BigDecimal.valueOf(-0.1);
            } else if (workingPosition > encoderPosition) {
                speed = BigDecimal.valueOf(0.1);
            } else if (workingPosition == 0) {
                return 0;
            }
            return speed.doubleValue();
        }
    }

    public void setTargetPosition(double position) {
        targetPosition = position;
    }

    public void stop() {
        elevatorMotor.set(0);
    }

    public double getPosition() {
        return elevatorEncoder.getPosition();
    }

    public boolean isAtTop() {
        // if (topLimitSwitch == null)
        // {
        // return topLimitSwitch.get() || elevatorMotor.get() <=
        // Constants.ElevatorConstants.ELEVATOR_L3;
        // }
        return elevatorMotor.get() > targetPosition;
    }

    public boolean isAtBottom() {
        return bottomLimitSwitch.get() && elevatorMotor.get() < targetPosition;
    }

    @Override
    public void close()  {
        elevatorMotor.close();
        bottomLimitSwitch.close();
    }

}
