/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

/**
 *
 * @author paul
 */
public class CollectorConvveyorFeedTimed extends CommandBase {
    
    private double m_timeout;
    
    public CollectorConvveyorFeedTimed(double time) {
        m_timeout = time;
        if (collector!=null) requires (collector);
        if (conveyer!=null) requires (conveyer);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(m_timeout);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        collector.startCollector();
        conveyer.startConveyer();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
        
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
