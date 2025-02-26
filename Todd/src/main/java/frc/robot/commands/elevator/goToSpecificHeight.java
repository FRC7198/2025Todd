package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevatorsubsystem;

public class goToSpecificHeight extends Command {

    @SuppressWarnings({"PMD.UnusedPrivateField", "PMD.SingularField"})
    private final Elevatorsubsystem elevatorSubsystem;
    private final double height;

    public goToSpecificHeight(Elevatorsubsystem elevatorsubsystemarg, double heightarg) {
        height = heightarg;
        elevatorSubsystem = elevatorsubsystemarg;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        elevatorSubsystem.setTargetPosition(height);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        elevatorSubsystem.setTargetPosition(height);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        elevatorSubsystem.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        if(elevatorSubsystem.getPosition() == height)
        {
            return true;
        }
        return false;
    }
}
