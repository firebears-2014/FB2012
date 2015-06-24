/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.RobotMap;
import org.firebears.commands.TurretRotateJoystick;

/**
 *
 * @author paul
 */
public class Turret extends PIDSubsystem {
    public static final double P = .25;//.25  
    public static final double I = .003;//.002;  
    public static final double D = 0.0;
    CANJaguar m_TurretRotate;
    AnalogChannel m_pot;
    double m_angle;//Process Variable
    double m_angleSP;//Angle setpoint

    public static final double POT_SCALE = 45;
    public static final double POT_OFFSET = 3.1719031529999997;//
    public static final double TURRET_MAX_CURRENT = 4.0;
    public static final double ANGLE_LIM = 35;
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
    
     
     
    public Turret(){
        super("Turret",P,I,D);
        
        m_pot = new AnalogChannel(RobotMap.TURRET_POT_CH);
        m_pot.setAverageBits(4); 
        
         try{
        m_TurretRotate = new CANJaguar(RobotMap.TURRET_ROTATE_JAG );
        m_TurretRotate.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
        
        }         
        catch (CANTimeoutException ex) {
        ex.printStackTrace();
        }       
        //getPIDController().setSetpointRange(-1.0,1.0);
         enable();
    }
    
    
    public void initDefaultCommand() {
        setDefaultCommand(new TurretRotateJoystick());
    }
    
    
     protected double returnPIDInput() {//Process variable
        return getTurretAngle();//m_angle
    }

    protected void usePIDOutput(double output) {//Where PID output goes
      if (RobotMap.DEBUG || RobotMap.TU_DEBUG)SmartDashboard.putNumber("PID Output = ",output);
        try { 
        m_TurretRotate.setX(-output);
       } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }   
    }
    
       
    public void rotateTurret(double angle){ //speed determined direction
        if(angle > ANGLE_LIM)angle = ANGLE_LIM;
        if(angle < -ANGLE_LIM)angle = -ANGLE_LIM;
       m_angleSP =  angle;
       setSetpoint(m_angleSP);
    }
        
     
      public void rotateWithStick(GenericHID stick){           
        m_angleSP = (stick.getX()) *ANGLE_LIM;//+- 35 degrees
        setSetpoint(m_angleSP);
        if (RobotMap.DEBUG || RobotMap.TU_DEBUG)SmartDashboard.putNumber("Angle Setpoint = ",m_angleSP);
      }
      
      
       public double getTurretAngle(){
         
          double volts = m_pot.getAverageVoltage();        
          m_angle = (volts -  POT_OFFSET) *POT_SCALE;
          
          // if(RobotMap.DEBUG){System.out.println("Pot Volts = " + volts);}
           if (RobotMap.DEBUG || RobotMap.TU_DEBUG) SmartDashboard.putNumber("Pot angle = ",m_angle);
           
          return m_angle;
     }
     

    public void displayPotOffset(){
        
        System.out.println("Pot offset voltage = " + m_pot.getAverageVoltage() );
    }
    
    
    public void updateStatus() {
        
    }
    
//    private double Limit(double moveValue){
//        
//        double current = 0.0;
//        //Get Jag current for limit
//        try{
//        current = m_TurretRotate.getOutputCurrent();
//        }
//        catch (CANTimeoutException ex) {
//            ex.printStackTrace();
//        }
//        
//        double angle = getTurretAngle();
//        
//        if (current> TURRET_MAX_CURRENT  || angle > 20.0 ){
//                moveValue = Math.max(0.0,moveValue);//assume speed is positive
//            }
//            if (current> TURRET_MAX_CURRENT  || angle < -20.0 ){
//                moveValue = Math.min(0.0,moveValue);//assume speed is negative
//            }
//        if(RobotMap.DEBUG){System.out.println("Turret Move Value = " + moveValue);}
//        
//        if(RobotMap.DEBUG){
//          System.out.println("Turret Speed = " + moveValue);
//          System.out.println("Turret Angle = " + angle);
//          System.out.println("Turret Current = " + current);
//          System.out.println("Pot Volts  = " + m_pot.getAverageVoltage());        
//          }
//        
//        return moveValue;
//    } 
}
