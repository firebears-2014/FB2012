/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

import org.firebears.RobotMap;

/**
 * Set the chassis speed multiplier to a specific value.
 */
public class ChassisSetSpeed extends CommandBase{

    final double multiplier;

    public ChassisSetSpeed(double m) {
        if (m < 0.0 || m>1.0) {
            throw new IllegalArgumentException("multiplier must be between 0 and 1");
        }
        this.multiplier = m;
        if(chassis != null)requires(chassis);
    }    

    protected void execute() {
        chassis.preferences.putDouble(RobotMap.CH_SPEED_MULT_KEY, this.multiplier);
        chassis.preferences.save();
        if (RobotMap.DEBUG) {System.out.println("::: SetChassisSpeed " + this.multiplier);}
    }

    protected boolean isFinished() {
        return true;
    }
        
    protected void initialize() {
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}

