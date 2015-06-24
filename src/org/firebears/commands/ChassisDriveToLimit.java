/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

import org.firebears.RobotMap;

/**
 *
 * @author paul
 */
public class ChassisDriveToLimit extends CommandBase {
    
     //AnalogChannel m_WallRangeFinderChannel;
     //UltraSonicSensor m_WallRangeFinder;
     double m_Limit;
     public static final double MIN_SPEED = .1; 
     public static final double START_DECEL = 80;//Start decel 60 inches from stop point
     double m_ScaleSpeed = (1.0-MIN_SPEED)/START_DECEL;//scale speed vs distance from limit (inches)
    
    public ChassisDriveToLimit(double limit) {
        m_Limit = limit;
        if(chassis != null)requires(chassis);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        chassis.straight(ExpDecel());
    }
    
    protected double ExpDecel(){
        
        double dSpeed = m_ScaleSpeed *(rangefinder.getDistance()- m_Limit) + MIN_SPEED;
        dSpeed = dSpeed*dSpeed;//bring down speed exponentially
        if(dSpeed < MIN_SPEED){//Enforce low limit
            dSpeed = MIN_SPEED;
        }
        if(dSpeed > 1.0 ){//enforce high limit
            dSpeed = 1.0;
        }
        return dSpeed;
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (RobotMap.DEBUG) {System.out.println("***************Entered Drive to limit ***********************");}
        if (rangefinder.getDistance() < m_Limit){//TODO Do we need to incorporate Camera here???
            if (RobotMap.DEBUG) {System.out.println("**Entered Drive to limit TRUE* " + rangefinder.getDistance());}
            return true;
        }
        else{
            if (RobotMap.DEBUG) {System.out.println("**Entered Drive to limit FALSE* " + rangefinder.getDistance());}
            return false;
        }
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}