/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO.EnhancedIOException;
import org.firebears.RobotMap;

/**
 *
 * @author paul
 * 
 * This command has 3 seconds for speed to reach setpoint within 5% before timing out.
 */
public class ShooterSpinnerSetSpeed extends CommandBase {

    //Shooter speed state
    private double m_timeout;
    static private int m_State = 0;
    private double m_speed;
    private String m_upDn;
    private double[] m_SpeedArray = { 26.0, 28, 30.0, 32, 34, 36.0, 38.0, 40.0, 42.0, 44.0,46.0, 48.0, 50.0, 52, 54.0, 56, 58};
    private static int m_arrayPos = 0;
    DriverStationEnhancedIO m_eIO = DriverStation.getInstance().getEnhancedIO();

    public ShooterSpinnerSetSpeed(String name, double speed) {
        super(name);
        if (shooter != null) {
            requires(shooter);
        }
        m_speed = speed;
        m_timeout = 3.0;
    }

    public ShooterSpinnerSetSpeed(double speed) {
        super();
        if (shooter != null) {
            requires(shooter);
        }
        m_speed = speed;
        m_timeout = 3.0;
    }

    public ShooterSpinnerSetSpeed(String UpDn) {//used with 
        super();
        if (shooter != null) {
            requires(shooter);
        }
        m_speed = SeqSpeed(UpDn);//Normal 3 speed controller
        //m_speed = SeqSpeedArray(UpDn);//For calibrating speed va distance, requires smart dashboard and SH_DEBUG = true
        m_upDn = UpDn;//??????
        //m_State = 0;//??????
        m_timeout = 3.0;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (m_upDn != null) {
//            m_speed = SeqSpeed(m_upDn);
            m_speed = SeqSpeedArray(m_upDn);//For calibrating speed va distance, requires smart dashboard and SH_DEBUG = true
            setTimeout(m_timeout);
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (RobotMap.DEBUG || RobotMap.SH_DEBUG) {
            System.out.println("Spinner Command : " + m_speed);
        }
        shooter.spin(m_speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (Math.abs(shooter.getShooterSpeed() - m_speed) / m_speed < .02 || isTimedOut()) {
            return true;
        } else {
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

    //Sequentially sets speed from up-down switches and controls console lights
    //Enters here with original m_State Class variable, looks for a up-dn switch, changes state if appropriate
    //then returns speed and lights the console LED depending on the resulting state.  
    protected double SeqSpeed(String UpDn) {//"Idle"
        if (UpDn == "Idle") {
            if (RobotMap.DEBUG) {
                System.out.println("Shooter Speed Idle");
            }
        }

        switch (m_State) {
            case 0://Minimum speed
                if (UpDn.equals("Up")) {
                    m_State = 1;
                }

                break;
            case 1://Low Shoot speed speed
                if (UpDn.equals("Up")) {
                    m_State = 2;
                }
                if (UpDn.equals("Idle")) {
                    m_State = 0;
                }

                break;
            case 2://Mid Shoot speed speed
                if (UpDn.equals("Up")) {
                    m_State = 3;
                }
                if (UpDn.equals("Dn")) {
                    m_State = 1;
                }
                if (UpDn.equals("Idle")) {
                    m_State = 0;
                }

                break;
            case 3://High Shoot speed speed
                if (UpDn.equals("Dn")) {
                    m_State = 2;
                }
                if (UpDn.equals("Idle")) {
                    m_State = 0;
                }

                break;
            default: {
                m_State = 0;
                return 15.0;
            }
        }
        if (m_State == 0) {//Idle, console switch on
            try {
                if (m_eIO != null) {
                    m_eIO.setDigitalOutput(RobotMap.CNSL_R_LOW_1, false);
                    m_eIO.setDigitalOutput(RobotMap.CNSL_B_HI_3, false);
                    m_eIO.setDigitalOutput(RobotMap.CNSL_W_MID_2, false);

                }
            } catch (EnhancedIOException ex) {
            }
            return 15.0;
        } else if (m_State == 1) {//Low Speed, close to basket, console switch off
            try {
                if (m_eIO != null) {
                    m_eIO.setDigitalOutput(RobotMap.CNSL_R_LOW_1, true);                    
                    m_eIO.setDigitalOutput(RobotMap.CNSL_W_MID_2, false);
                    m_eIO.setDigitalOutput(RobotMap.CNSL_B_HI_3, false);
                }
            } catch (EnhancedIOException ex) {
            }
            return 35.0;
        } else if (m_State == 2) {//Mid Speed, In safe zone, console switch off
            try {
                if (m_eIO != null) {
                    m_eIO.setDigitalOutput(RobotMap.CNSL_R_LOW_1, false);                    
                    m_eIO.setDigitalOutput(RobotMap.CNSL_W_MID_2, true);
                    m_eIO.setDigitalOutput(RobotMap.CNSL_B_HI_3, false);
                }
            } catch (EnhancedIOException ex) {
            }
            return 40.0;
        } else if (m_State == 3) {// High speed, let er rip, console switch off
            try {
                if (m_eIO != null) {
                    m_eIO.setDigitalOutput(RobotMap.CNSL_R_LOW_1, false);                   
                    m_eIO.setDigitalOutput(RobotMap.CNSL_W_MID_2, false);
                    m_eIO.setDigitalOutput(RobotMap.CNSL_B_HI_3, true);
                }
            } catch (EnhancedIOException ex) {
            }
            return 45.0;
        } else {
            return 45.0;
        }

    }
    //Used to calibrate speed vs distance

    private double SeqSpeedArray(String UpDn) {
        int arraySize = m_SpeedArray.length;
        if (UpDn.equals("Up") && m_arrayPos < arraySize - 1) {
            m_arrayPos++;
        }
        if (UpDn.equals("Dn") && m_arrayPos > 0) {
            m_arrayPos--;
        }
        return m_SpeedArray[m_arrayPos];
    }
}