/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

/**
 *
 * @author paul
 */
public class AirCompressorEnable extends CommandBase {
    
    public AirCompressorEnable() {
    if(airCompressor != null)requires(airCompressor);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {       
        airCompressor.compressorEnable();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {//default runs unless disabled
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
