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
 * @author Javad
 */
public class Conveyer extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public static final double MAX_FWD = -1.0;
    public static final double MAX_REV = 1.0;
    DigitalInput m_ShooterBallSensor;
    
    CANJaguar m_conveyer;
    
    
    public Conveyer () {
        try {
           
            m_conveyer  = new CANJaguar(RobotMap.CONVEYER_JAG);
            m_conveyer.changeControlMode(CANJaguar.ControlMode.kPercentVbus);;
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        } 
        m_ShooterBallSensor = new DigitalInput(RobotMap.BALL_IN_SHOOTER_CH);
        //m_ShooterBallSensor.get();
    }
        
    public void initDefaultCommand() {
        //setDefaultCommand(new ConveyerFeed());//Requires ball sensor stop logic
        setDefaultCommand(new ConveyerStop());
    }
    
    public void startConveyer(){
        try{
            
            //TODO if ball in shooter chamber, stop conveyer
       
        m_conveyer.setX(MAX_FWD);
        
        
        
        if (RobotMap.DEBUG) {System.out.println("Conveyer is running In");}
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();   
        }   
    }
   
    
    public void stopConveyer(){
        try{
        
        m_conveyer.setX(0.0);
        if (RobotMap.DEBUG) {System.out.println("Conveyer is Stopped");}
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
     
//    public void starCollectorEject(){
//        try{
//        m_collector.setX(MAX_FWD);
//        //m_elevatorConv.setX(MAX_FWD);
//        if (RobotMap.DEBUG) {System.out.println("Elevator is running Eject");}
//        }
//        catch (CANTimeoutException ex) {
//            ex.printStackTrace();
//        }
//    }
    
//    public void startConveyer(double time){
//        try{
//        m_collector.setX(MAX_REV);
//        m_conveyer.setX(MAX_REV);
//        System.out.println("Conveyer is running In");
//        }
//        catch (CANTimeoutException ex) {
//            ex.printStackTrace();   
//        }   
//    }
    
//    public void stopConveyer(){
//        try{
//        m_collector.setX(0.0);
//        m_conveyer.setX(0.0);
//        System.out.println("Conveyer is Stopped");
//        }
//        catch (CANTimeoutException ex) {
//            ex.printStackTrace();
//        }
    //}
    
}
