package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import java.math.BigDecimal;
// import java.util.logging.Level;
// import java.util.logging.Logger;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.ElevatorConstants;



public class Elevatorsubsystem extends SubsystemBase implements AutoCloseable {

    // Logger logger = Logger.getLogger(Elevatorsubsystem.class.getName());

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

        Double speed = calculateElevatorMotorSpeed(elevatorEncoder.getPosition(), false, false);
        if (speed == 0) {
            stop();
        } else {
            elevatorMotor.set(speed); // Adjust speed as needed
        }

        SmartDashboard.putNumber("elevatorspeed", speed);
        SmartDashboard.putNumber("targetposition", targetPosition);
        SmartDashboard.putNumber("elevatorencoderpos", elevatorEncoder.getPosition());
        SmartDashboard.putBoolean("isattop", isAtTop(elevatorEncoder.getPosition(), false));
        SmartDashboard.putBoolean("isatbottom", isAtBottom(elevatorEncoder.getPosition(), false));

    }

    public double calculateElevatorMotorSpeed(double encoderPosition, boolean topLimitSwitch, boolean bottomLimitSwitch) { 
        
        // Stop motor if limit switch is triggered
        BigDecimal speed = new BigDecimal(0.0);
        // logger.log(Level.INFO,String.format("targetPosition: "+ targetPosition + " encoderPosition: "+encoderPosition));


        //Are we at the highest elevator point according to the encoder or have we tripped the top limit switch
        if (isAtTop(encoderPosition, topLimitSwitch)) {
            return 0;
        //Are we at the lowest elevator point according to the encoder or have we tripped the bottom limit switch
        } else if (isAtBottom(encoderPosition, bottomLimitSwitch)) {
            return 0;
        } else {
            // Move towards target position

            //work out how far from the position we want to go is
            double workingPosition = targetPosition - encoderPosition;
            SmartDashboard.putNumber("workingPosition", workingPosition);
            // logger.log(Level.INFO,String.format("workingPosition: "+ workingPosition));
            // Are we above where we need to go and are we farther away than one encoder pulse?
            if (encoderPosition < targetPosition &&  Math.abs(workingPosition) > 1)
            {
                // logger.log(Level.INFO,String.format("Go Up"));
                // Go Up
                speed = BigDecimal.valueOf(0.1);
                
            //Are we below we we need to go and are we farther away than 1 encoder pulse?
            } else if (encoderPosition > targetPosition && Math.abs(workingPosition) > 1) {
                // Go Up
                speed = BigDecimal.valueOf(-0.1);
            } else if(Math.abs(workingPosition) < 1) {
                // logger.log(Level.INFO,String.format("We are close so stop"));
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

    public boolean isAtTop(double encoderPosition, boolean topLimitSwitch) {
        // if (topLimitSwitch == null)
        // {
        // return topLimitSwitch.get() || elevatorMotor.get() <=
        // Constants.ElevatorConstants.ELEVATOR_L3;
        // }
        Boolean isAtTop = encoderPosition < Constants.ElevatorConstants.ELEVATOR_L3;
        // logger.log(Level.INFO,String.format("isAtTop: "+ isAtTop.toString()));
        return isAtTop;
    }

    public boolean isAtBottom(double encoderPosition, boolean bottomLimitSwitch) {
        Boolean isAtBottom = encoderPosition > Constants.ElevatorConstants.ELEVATOR_BOTTOM_POSITION;
        // logger.log(Level.INFO,"isAtBottom: "+ isAtBottom.toString());
        return isAtBottom;
    }

    @Override
    public void close()  {
        elevatorMotor.close();
        bottomLimitSwitch.close();
    }

}
