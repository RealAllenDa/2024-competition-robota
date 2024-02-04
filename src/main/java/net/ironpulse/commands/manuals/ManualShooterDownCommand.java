package net.ironpulse.commands.manuals;

import edu.wpi.first.wpilibj2.command.Command;
import net.ironpulse.Constants;
import net.ironpulse.subsystems.ShooterSubsystem;

public class ManualShooterDownCommand extends Command {
    private final ShooterSubsystem shooterSubsystem;

    public ManualShooterDownCommand(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        //addRequirements(shooterSubsystem);
    }

    @Override
    public void execute() {
        shooterSubsystem.getArmMotor()
                .setVoltage(Constants.ShooterConstants.manualAimingVoltage.magnitude());
    }

    @Override
    public void end(boolean interrupted) {
        shooterSubsystem.getArmMotor().setVoltage(0);
    }
}
