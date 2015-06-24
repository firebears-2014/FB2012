/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

/**
 *
 * @author paul
 */
public class TurretTurnReqDegrees extends CommandBase {
    double m_GoToDegrees;
    double m_DegreeChangeReq;
    
    public TurretTurnReqDegrees(double DegreeChangeReq) {
        // Use requires() here to declare subsystem dependencies
        if(turret != null)requires(turret);
        m_DegreeChangeReq = DegreeChangeReq;
       
    }

    // Called just before this Command runs the first time
    protected void initialize() {
         m_GoToDegrees = turret.getTurretAngle() + m_DegreeChangeReq;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
       
            turret.rotateTurret( m_GoToDegrees);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(Math.abs(turret.getTurretAngle() - m_GoToDegrees) < 0.35){           
        return true;
        }
        else {return false;}
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
