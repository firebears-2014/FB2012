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
public class WedgeDeploy extends CommandBase {
    
    public WedgeDeploy() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        if (wedge != null)requires (wedge);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (RobotMap.DEBUG) {System.out.println("initialize");}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        wedge.wedgeDeploy();
        if (RobotMap.DEBUG) {System.out.println("/WedgeDeploy.execute");}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean bFin = !oi.btnDrWedgeDeployD1.get();
        if (RobotMap.DEBUG) {System.out.println("/WedgeDeploy is finished  = " + bFin);}
        return bFin;//true when button released
    }

    // Called once after isFinished returns true
    protected void end() {
        if (RobotMap.DEBUG) {System.out.println("end");}
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }

   
}
