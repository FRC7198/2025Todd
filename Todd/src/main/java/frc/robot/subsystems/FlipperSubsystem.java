package frc.robot.subsystems;

import java.math.BigDecimal;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.FlipperConstants;
import frc.robot.commands.FlipCommand;

public class FlipperSubsystem extends SubsystemBase implements AutoCloseable {

    private SparkMax flipperMotor;
    private RelativeEncoder flipperEncoder;
    private FlipperState flipperState = FlipperState.RESET;
    private boolean shouldBeFlipping = false;

    public enum FlipperState {
        FLIPPING, // flipping the tongue out
        RETURNING, // moving back to reset position
        RESET // down and waiting to be flipped again
    }

    public FlipperSubsystem() {
        flipperMotor = new SparkMax(Constants.FlipperConstants.FLIPPER_MOTOR_ID, MotorType.kBrushless);
        flipperEncoder = flipperMotor.getEncoder();
    }

    public FlipperState getFlipperState() {
        return flipperState;
    }

    public void setFlipperState(FlipperState flipperState) {
        this.flipperState = flipperState;
    }

    public boolean getIsInFlipCycle() {
        return shouldBeFlipping;
    }

    @Override
    public void periodic() {

        //if we are not in a flip command no need to calculcate speed
        if (!shouldBeFlipping) {
            return;
        }
        // This method will be called once per scheduler run
        double flipperMotorSpeed = flip(flipperEncoder.getPosition());
        if (flipperMotorSpeed == 0) {
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

        if (!shouldBeFlipping) {
            return 0; // we shouldn't be flipping stop us
        }

        switch (flipperState) {
            // if we are in the reset position
            case RESET:
                // as we've been told to flip begin flipping the flipper
                flipperState = FlipperState.FLIPPING;
                return Constants.FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue();
            case FLIPPING:
                // have we finsihed moving to the flipped position
                if (flipperPosition >= Constants.FlipperConstants.FLIPPER_TILT_POSITION) {
                    // if so then have us start moving back
                    flipperState = FlipperState.RETURNING;
                    return FlipperConstants.FLIPPER_MOTOR_BACK_SPEED.doubleValue();
                } else {
                    return FlipperConstants.FLIPPER_MOTOR_FORWARD_SPEED.doubleValue();
                }
            case RETURNING:
                // have we finsihed moving to the flipped position
                if (flipperPosition <= Constants.FlipperConstants.FLIPPER_STARTING_POSITION) {
                    shouldBeFlipping = false;
                    flipperState = FlipperState.RESET;
                    // if so then stop and we should be reset
                    return 0;
                } else {
                    // continue resetting tongue
                    return FlipperConstants.FLIPPER_MOTOR_BACK_SPEED.doubleValue();
                }
            default:
                return 0;
        }
    }

    // called via command
    public void RunFlipCycle() {
        shouldBeFlipping = true;
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

    @Override
    public void close() {
        flipperMotor.close();
    }

}
