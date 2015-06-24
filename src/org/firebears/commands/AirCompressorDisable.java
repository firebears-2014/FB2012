/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

import org.firebears.OI;

/**
 *
 * @author Paul
 */
public class AirCompressorDisable extends CommandBase {
    
    
    
    public AirCompressorDisable() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        if(airCompressor != null)requires(airCompressor);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        airCompressor.compressorDisable();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return !OI.btnConsoleDisableCompressor.get();//Return true when button released      
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
