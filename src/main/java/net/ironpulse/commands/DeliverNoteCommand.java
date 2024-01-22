package net.ironpulse.commands;

import edu.wpi.first.wpilibj2.command.Command;
import net.ironpulse.Constants;
import net.ironpulse.RobotContainer;
import net.ironpulse.subsystems.IndexerSubsystem;

import static net.ironpulse.state.StateMachine.*;

public class DeliverNoteCommand extends Command {
    private final IndexerSubsystem indexerSubsystem;
    private final RobotContainer robotContainer;

    public DeliverNoteCommand(IndexerSubsystem indexerSubsystem, RobotContainer robotContainer) {
        this.indexerSubsystem = indexerSubsystem;
        this.robotContainer = robotContainer;
        addRequirements(indexerSubsystem);
    }

    @Override
    public void execute() {
        indexerSubsystem.getIndexerMotor()
                .setVoltage(Constants.IndexerConstants.indexVoltage.magnitude());
    }

    @Override
    public void end(boolean interrupted) {
        if (interrupted) {
            robotContainer.getGlobalState().transfer(Actions.INTERRUPT_SHOOT);
        }
        indexerSubsystem.getIndexerMotor().setVoltage(0);
    }

    @Override
    public boolean isFinished() {
        return robotContainer.getGlobalState().getCurrentState() == States.IDLE;
    }
}