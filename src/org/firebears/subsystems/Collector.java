/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.firebears.RobotMap;
import org.firebears.commands.ConveyerStop;

/**
 *
 * @author paul
 */
public class Collector extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public static final double MAX_FWD = -1.0;
    public static final double MAX_REV = 1.0;
    DigitalInput m_CollectorBallSensor;
    CANJaguar m_collector;


    public void initDefaultCommand() {
        // setDefaultCommand(new CollectorFeed());//requires ball sensor stop
        setDefaultCommand(new ConveyerStop());
    }
    public Collector () {
        try {
            m_collector = new CANJaguar(RobotMap.ELEVATOR_SUCKER_JAG);
            m_collector.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            m_CollectorBallSensor = new DigitalInput(RobotMap.BALL_IN_COLLECTOR_CH);
            
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }  
    }
        
   
    
    public void startCollector(){
        try{
        m_collector.setX(MAX_FWD);

        
        //TODO if ball in collector chamber, stop collector
        
        if (RobotMap.DEBUG) {System.out.println("Elevator is running In");}
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();   
        }   
    }
    
    public void startCollectorEject(){
        try{
        m_collector.setX(MAX_REV);
        //m_elevatorConv.setX(MAX_FWD);
        if (RobotMap.DEBUG) {System.out.println("Elevator is running Eject");}
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void stopCollector(){//May not need this
        try{
        m_collector.setX(0.0);

        if (RobotMap.DEBUG) {System.out.println("Elevator is Stopped");}
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
}
