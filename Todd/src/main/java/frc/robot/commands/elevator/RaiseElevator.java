package frc.robot.commands.elevator;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Elevatorsubsystem;

public class RaiseElevator extends Command {

    Elevatorsubsystem elevatorsubsystem;

    public RaiseElevator(Elevatorsubsystem elevatorsubsystem) {
        this.elevatorsubsystem = elevatorsubsystem;
    }
    
  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    elevatorsubsystem.raise();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    elevatorsubsystem.triggerReset();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
    
}
