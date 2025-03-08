package frc.robot.commands;


import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FlipperSubsystem;

public class FlipCommand extends Command {

    private FlipperSubsystem flipperSubsystem;

    public FlipCommand(FlipperSubsystem flipperSubsystem) {
        this.flipperSubsystem = flipperSubsystem;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
            flipperSubsystem.RunFlipCycle();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}
