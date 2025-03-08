package frc.robot.subsystems;


import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.FlipCommand;

public class FlipperSubsystem extends SubsystemBase {

    private SparkMax flipperMotor;
    private RelativeEncoder flipperEncoder;
    private FlipperState flipperState = FlipperState.RESET;
    private boolean shouldBeFlipping = false;

    private enum FlipperState {
        FLIPPING, // flipping the tongue out
        RETURNING, // moving back to reset position
        RESET // down and waiting to be flipped again
    }

    public FlipperSubsystem() {
        flipperMotor = new SparkMax(Constants.FlipperConstants.FLIPPER_MOTOR_ID, MotorType.kBrushless);
        flipperEncoder = flipperMotor.getEncoder();
    }

    @Override
    public void periodic() {

        // This method will be called once per scheduler run
        double flipperMotorSpeed = flip(flipperEncoder.getPosition());
        if(flipperMotorSpeed == 0) {
            flipperMotor.stopMotor();
        } else {
            flipperMotor.set(flipperMotorSpeed);
        }

    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run during simulation
    }

    // 
    public double flip(double flipperPosition) {

        if(!shouldBeFlipping) {
            return 0; // we shouldn't be flipping stop us 
        }
        //if we are in the reset position
        if(flipperState == FlipperState.RESET) {

            // as we've been told to flip begin flipping the flipper
            flipperState = FlipperState.FLIPPING;
            return Constants.FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue();

        } else if(flipperState == FlipperState.FLIPPING) {

            // have we finsihed moving to the flipped position
            if(flipperPosition >= Constants.FlipperConstants.FLIPPER_TILT_POSITION) {
                //if so then have us start moving back
                //TODO: set the flipperstate and set the speed to run the motor backwards
                return 0; //TODO to be removed
            } else {
                //TODO continue flipping by running the motor forwards
                return 0; //TODO to be removed
            }

        } else if( flipperState == FlipperState.RETURNING) {
            // have we finsihed moving to the flipped position
            if(flipperPosition <= Constants.FlipperConstants.FLIPPER_STARTING_POSITION) {
                //if so then stop and we should be reset
                return 0;
            } else {
                //TODO return the speed to move the motor backwards
                return 0; //TODO to be removed
            }
        }

        //TODO What should we do if we haven't hit any of the above? what should the default be
        return 0; //TODO to be removed
    }

    //called via command
    public void RunFlipCycle() {
        //TODO start a flip cycle
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
