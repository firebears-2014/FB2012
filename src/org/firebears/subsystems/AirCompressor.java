/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.firebears.RobotMap;
import org.firebears.commands.AirCompressorEnable;

/**
 *
 * @author Javad
 * m_armHeightIn = new AnalogChannel(3);

    DigitalInput compressorPressureSW    m_leftLineIn = new DigitalInput(1);
 */
public class AirCompressor extends Subsystem {
    Compressor compressor;
    //DigitalInput DI1 = new DigitalInput(RobotMap.COMPRES_PS_CH);
    //DigitalOutput DO14 = new DigitalOutput(RobotMap.COMPRES_OUT_CH);
   
    
    public AirCompressor(){
       compressor = new Compressor(RobotMap.COMPRES_PS_CH, RobotMap.COMPRES_OUT_CH);
       //compressor.start();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
       setDefaultCommand(new AirCompressorEnable());
    }
    
    public void compressorDisable(){
        if (RobotMap.ARM_DEBUG) {System.out.println("Disabling compressor");}
        compressor.stop();//This really means Disable
    }
    
    public void compressorEnable(){
       if (RobotMap.ARM_DEBUG) {System.out.println("Enabling compressor");}
        compressor.start();
    }
}
