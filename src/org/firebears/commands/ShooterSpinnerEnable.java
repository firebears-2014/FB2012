/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

/**
 *
 * @author paul
 */
public class ShooterSpinnerEnable extends CommandBase {
    
    public ShooterSpinnerEnable() {
        // Use requires() here to declare subsystem dependencies
        if (shooter != null)requires(shooter);//TODO !!!!Need to incorporate speed switching commands
        //TODO !!!!Distance vs speed command??
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        shooter.spin(1.0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
