/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.firebears.RobotMap;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.commands.TurretRotateJoystick;

/**
 *
 * @author paul
 */
public class TurretOld extends Subsystem {
     CANJaguar m_TurretRotate;
     AnalogChannel m_pot;
     double m_angle;
     
     double POT_SCALE = 40;
     double POT_OFFSET = 3.217;//
     double TURRET_MAX_CURRENT = 4.0;//
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
     
     public TurretOld(){
        try{
        m_TurretRotate = new CANJaguar(RobotMap.TURRET_ROTATE_JAG );
        //m_TurretRotate.configNeutralMode(CANJaguar.NeutralMode.kBrake);
       // m_TurretRotate.disableControl();
        //m_TurretRotate.changeControlMode(CANJaguar.ControlMode.kPercentVbus);//may change to PID
       // m_TurretRotate.setPID(.5, .001, 0.0);
       // m_TurretRotate.enableControl();//Can initialize encoder with this?
        //m_TurretRotate.

        }
          
        catch (CANTimeoutException ex) {
        ex.printStackTrace();
        }
        m_pot = new AnalogChannel(RobotMap.TURRET_POT_CH);
        m_pot.setAverageBits(2);
     }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new TurretRotateJoystick());
        //setDefaultCommand(new TurretCenter());
    }
    
     public void rotateTurret(double speed){ //speed determined direction 
         double rate;
         rate = speed;
         double angle = getTurretAngle();
          if(RobotMap.DEBUG){System.out.println("Turret Speed = " + speed);}
          if(RobotMap.DEBUG){System.out.println("Turret Angle = " + angle);}
        try{           
            double current = m_TurretRotate.getOutputCurrent();
            
            if (current> TURRET_MAX_CURRENT  || angle > 20 ){
                rate = Math.min(0.0,speed);//assume speed is positive
            }
            if (current> TURRET_MAX_CURRENT  || angle < -20 ){
                rate = Math.max(0.0,speed);//assume speed is negative
            }
            m_TurretRotate.setX(-rate);
        }                
         catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }            
    }
     //Scale pot to degrees, center = 0.0
     public double getTurretAngle(){
         
         
         if(RobotMap.DEBUG){System.out.println("Pot Volts = " + m_pot.getAverageVoltage());}
          m_angle = (m_pot.getAverageVoltage()-  POT_OFFSET) *POT_SCALE;
         if (RobotMap.DEBUG) SmartDashboard.putNumber("Pot angle = ",m_angle);
          return m_angle;
     }
    
    public void rotateWithStick(GenericHID stick){       
        double moveValue = (stick.getX());
        double current = 0.0;
        try{
        current = m_TurretRotate.getOutputCurrent();
        }
        catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        double angle = getTurretAngle();
        if(RobotMap.DEBUG){
          System.out.println("Turret Speed = " + moveValue);
          System.out.println("Turret Angle = " + angle);
          System.out.println("Turret Current = " + current);
          System.out.println("Pot Volts  = " + m_pot.getAverageVoltage());        
          }
        if (current> TURRET_MAX_CURRENT  || angle > 20 ){
                moveValue = Math.max(0.0,moveValue);//assume speed is positive
            }
            if (current> TURRET_MAX_CURRENT  || angle < -20 ){
                moveValue = Math.min(0.0,moveValue);//assume speed is negative
            }
        if(RobotMap.DEBUG){System.out.println("Turret Move Value = " + moveValue);}
        try{
            m_TurretRotate.setX(moveValue);
          
        }                
         catch (CANTimeoutException ey) {
            ey.printStackTrace();
        }                 
        }
    public void SetOffset(){
        POT_OFFSET = m_pot.getAverageVoltage();
    }
    
    
   
   
}
