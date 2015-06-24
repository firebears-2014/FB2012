/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

import org.firebears.RobotMap;

/**
 *
 * @author Ben
 */
public class WedgeRetract extends CommandBase {
    
    private double m_timeout;
    
    public WedgeRetract() {
         m_timeout = 1.0;
        if (wedge != null)requires (wedge);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(m_timeout);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      if (RobotMap.ARM_DEBUG) {System.out.println("RaiseWedgeArm.Execute");}
        wedge.wedgeRetract();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //if(isTimedOut()){
         //wedge.wedgeRest();   
         return true;   
        //}
        //else {return false;}
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
