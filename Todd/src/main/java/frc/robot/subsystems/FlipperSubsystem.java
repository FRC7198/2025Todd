package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.FlipCommand;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

 import java.util.logging.Level;
import java.util.logging.Logger;
public class FlipperSubsystem extends SubsystemBase {

    Logger logger = Logger.getLogger(FlipperSubsystem.class.getName());


    private SparkMax flipperMotor;
    private RelativeEncoder flipperEncoder;
    private String flipperState = "";
    private final Timer otherTimer = new Timer();

    public FlipperSubsystem() {
        flipperMotor = new SparkMax(Constants.FlipperConstants.FLIPPER_MOTOR_ID, MotorType.kBrushless);
        flipperEncoder = flipperMotor.getEncoder();
    }

    @Override
    public void periodic() {
        if (flipperState == "flipping") {
            flipperMotor.set(Constants.FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue());
        } else if (flipperState == "returning") {
            flipperMotor.set(Constants.FlipperConstants.FLIPPER_MOTOR_BACK_SPEED.doubleValue());
        } else if (flipperState == "stopped") {
            flipperMotor.set(0);
        } else {
            //logger.log(Level.INFO,String.format("Flipper state undetermined"));
        }
        SmartDashboard.putString("flipperstate", flipperState);
        // This method will be called once per scheduler run
/* 
        if (flipperState == "flipping")
        {
            if (flipperEncoder.getPosition() == Constants.FlipperConstants.FLIPPER_TILT_POSITION) {
                flipperMotor.stopMotor();
                otherTimer.restart();
                if (otherTimer.hasElapsed(5)) {
                    flipperState = "returning";
                    }
            } else {
                flipperMotor.set(Constants.FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue());
            }
        } else if (flipperState == "returning") {
            if (flipperEncoder.getPosition() == Constants.FlipperConstants.FLIPPER_STARTING_POSITION)
            {
                flipperMotor.stopMotor();
                flipperState = "reset";
            } else {
                flipperMotor.set(Constants.FlipperConstants.FLIPPER_MOTOR_BACK_SPEED.doubleValue());
            }
        } else if (flipperState == "reset") {
            flipperMotor.stopMotor();
        }
        */
      //  SmartDashboard.putNumber("currentDraw", currentDraw);
        SmartDashboard.putNumber("encoderPosition", flipperEncoder.getPosition());
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }

    //called via command
    public void RunFlipCycle() {
            logger.log(Level.INFO,String.format("Flip"));
            flipperState = "flipping";
        
        otherTimer.restart();
        if (otherTimer.hasElapsed(1)) {
            flipperState = "stopped";
            
        }
        otherTimer.restart();
        if (otherTimer.hasElapsed(5)) {
            flipperState = "returning";
            
        }
        otherTimer.restart();
        if (otherTimer.hasElapsed(1)) {
            flipperState = "stopped";
            
        }
        /* 
        flipperState = "flipping";
        while (flipperState == "flipping")
        {
            if (flipperEncoder.getPosition() == Constants.FlipperConstants.FLIPPER_TILT_POSITION) {
                flipperMotor.stopMotor();
                otherTimer.restart();
                if (otherTimer.hasElapsed(5)) {
                    flipperState = "returning";
                    }
            } else if (flipperEncoder.getPosition() != Constants.FlipperConstants.FLIPPER_TILT_POSITION) {
                flipperMotor.set(Constants.FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue());
            }
        }
        while (flipperState == "returning") {
            if (flipperEncoder.getPosition() == Constants.FlipperConstants.FLIPPER_STARTING_POSITION)
            {
                flipperMotor.stopMotor();
                flipperState = "reset";
            } else if (flipperEncoder.getPosition() != Constants.FlipperConstants.FLIPPER_STARTING_POSITION) {
                flipperMotor.set(Constants.FlipperConstants.FLIPPER_MOTOR_BACK_SPEED.doubleValue());
            }
        }    */
    }

    /**
     * Example command factory method.
     *
     * @return a command
     */
    public Command getFlipCommand() {
        // Inline construction of command goes here.
        // Subsystem::RunOnce implicitly requires `this` subsystem.
        return runOnce(
                () -> {
                    /* one-time action goes here */
                    new FlipCommand(this);
                });
    }

}
