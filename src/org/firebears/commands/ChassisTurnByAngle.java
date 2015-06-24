package org.firebears.commands;

import org.firebears.RobotMap;


/**
 * Turn Chassis by a specific angle.
 * 
 * @author krieck
 */
public class ChassisTurnByAngle extends CommandBase {

    private final double m_turnAngle;
    private boolean m_finished = false;
    private double m_targetAngle;
    private int m_max_iters = 1000;
    private final double MARGIN = 2.0;

    public ChassisTurnByAngle(String name, double angle) {
        super(name);
        m_turnAngle = angle;
        if(chassis != null)requires(chassis);
    }
    
    public ChassisTurnByAngle(double angle) {
        super();
        m_turnAngle = angle;
        if (chassis != null) requires(chassis);
    }

    protected void initialize() {
        double currentAngle = chassis.getGyroAngle();
        m_targetAngle = chassis.fixAngle(currentAngle - m_turnAngle, 360);
        m_finished = false;
        m_max_iters = 1000;
        if (RobotMap.DEBUG) {System.out.println("::: ChassisTurnByAngle.initialize: currentAngle="
                + currentAngle + "  : targetAngle" + m_targetAngle);}
    }

    protected void execute() {
        double angleDiff = chassis.getGyroAngleTo(m_targetAngle);
        
        if (Math.abs(angleDiff) < MARGIN) {
            m_finished = true;
            chassis.stop();
        } else {
            boolean clockwise = angleDiff > 0;
            chassis.turnSlowly(clockwise);
//          System.out.println("::: " + this + " : currentAngle=" + chassis.getGyroAngle() 
//                    + " : angleDiff=" + angleDiff
//                    + " : clockwise=" + clockwise);
        }
        m_max_iters--;
    }

    protected boolean isFinished() {
        if (m_max_iters <= 0) {
            System.err.println("::: " + this + " : too many iterations : ");
            return true;
        }
        return m_finished;
    }

    protected void end() {
    if (RobotMap.DEBUG) {System.out.println("::: ChassisTurnByAngle.end: " + chassis.getGyroAngle());}
        chassis.stop();
    }

    protected void interrupted() {
    if (RobotMap.DEBUG) { System.out.println("::: ChassisTurnByAngle.interupted: " + chassis.getGyroAngle());}
        chassis.stop();
    }

    public String toString() {
        return "ChassisTurnByAngle[" + m_turnAngle + "]";
    }
}
