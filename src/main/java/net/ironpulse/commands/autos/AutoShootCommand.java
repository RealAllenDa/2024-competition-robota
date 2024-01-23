package net.ironpulse.commands.autos;

import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import net.ironpulse.subsystems.IndexerSubsystem;
import net.ironpulse.subsystems.ShooterSubsystem;

public class AutoShootCommand extends ParallelCommandGroup {
    public AutoShootCommand(
            ShooterSubsystem shooterSubsystem,
            IndexerSubsystem indexerSubsystem
    ) {
        addCommands(
                new AutoAimingCommand(shooterSubsystem),
                new AutoPreShootCommand(shooterSubsystem),
                Commands.sequence(
                        new WaitCommand(2),
                        new AutoDeliverNoteCommand(indexerSubsystem)
                )
        );
    }
}