package org.firebears.commands;

import org.firebears.OI;

/**v
 *
 * @author Paul
 */
public class ConveyerRun extends CommandBase {

    public ConveyerRun() {
        if (conveyer != null) {
            requires(conveyer);
        }
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        conveyer.startConveyer();

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}