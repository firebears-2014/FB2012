package org.firebears.commands;

/**
 *
 * @author Paul
 */
public class CollectorEject extends CommandBase {
    
    public CollectorEject() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        if (collector!=null) requires (collector);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        collector.startCollectorEject();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !oi.btnCollectorEjectLS11.get();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
