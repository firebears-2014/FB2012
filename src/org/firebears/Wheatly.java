/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.firebears;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.commands.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Wheatly extends IterativeRobot {

    Command autonomousCommand;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        // instantiate the command used for the autonomous period
        // 
        autonomousCommand = new Auto2();

        // Initialize all subsystems
        CommandBase.init();
    }

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        CommandBase.updateStatus();
        testEnhancedIO();
    }

    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        CommandBase.updateStatus();
        testEnhancedIO();
    }

    /**
     * This function is called periodically during disabled mode.
     */
    public void disabledPeriodic() {
        CommandBase.updateStatus();
        testEnhancedIO();
    }
    
    public void disabledInit() {
        System.out.println("\n\nRobot loaded and ready for action\n\n");
    }
    
    
    
    DriverStationEnhancedIO m_eIO = DriverStation.getInstance().getEnhancedIO();
    protected void testEnhancedIO() {
        int channel = 0;
        try {
            boolean compressorSwitch = m_eIO.getDigital(4);
            boolean shooterSwitch = m_eIO.getDigital(6);
            
            SmartDashboard.putBoolean("Compressor ", compressorSwitch);
            SmartDashboard.putBoolean("Shoot ", shooterSwitch );
            
            m_eIO.setDigitalOutput(RobotMap.CNSL_R_LOW_1, compressorSwitch && !shooterSwitch);
                     
            m_eIO.setDigitalOutput(RobotMap.CNSL_B_HI_3, shooterSwitch && !compressorSwitch);
            
            m_eIO.setDigitalOutput(RobotMap.CNSL_W_MID_2, shooterSwitch &&  compressorSwitch);
            
            m_eIO.setDigitalOutput(RobotMap.CNSL_G_TRACK, !shooterSwitch &&  !compressorSwitch);
            m_eIO.setDigitalOutput(RobotMap.CNSL_OrRed_NOT_TRACK, !shooterSwitch &&  !compressorSwitch);
            
            

        } catch (Exception e) {
           // System.err.println(channel + " ::: " + e.getMessage());
        }
    }
    
    public void testInit() {
        System.out.println("\n\nEntering test mode\n");
    }

    public void testPeriodic() {
        LiveWindow.run();
    }
}
