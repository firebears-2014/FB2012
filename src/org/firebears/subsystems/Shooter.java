
package org.firebears.subsystems;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import org.firebears.RobotMap;
import org.firebears.commands.ShooterSpinnerSetSpeed;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 *
 * @author paul
 */
public class Shooter extends PIDSubsystem {
     
    //public class Shooter extends Subsystem {
    // Put methods for controlling this subsystem

    public static final double P = .15;//.15  Spinner tuning constants work best above 30
    public static final double I = .0025;//.0025;  A little unstable below 20
    public static final double D = 0.0;
    public static final double PID_Y_SPEED_SCALE = 100.0;
    public static final double SPD_MAX = 1;
    public static final double SPD_MIN = .33;
    public static final double SAMPLE_TIME = 500;//milliseconds (half second)
   
    CANJaguar m_SpinUpper;
    Counter m_RotateSpeed;
    double m_speedReq;
    double m_Period;
    double m_scaledProcessVar = 0.0;
    double m_oldScaledPV = 0.0;
    
    Solenoid m_shootfireSol = new Solenoid(RobotMap.SHOOT_FIRE_CH);
    Solenoid m_shootRetractSol = new Solenoid(RobotMap.SHOOT_RETRACT_CH);
       
    
    
    
    public Shooter(){
        super("Shooter", P, I, D);
        
        m_RotateSpeed = new Counter(RobotMap.SPIN_COUNTER_CH);//SPIN_COUNTER_CH = 2;
        m_RotateSpeed.setUpDownCounterMode();
       
        //Initialize counter and count timer
        m_RotateSpeed.start(); 
        //m_startTime = System.currentTimeMillis();
            
        try {        
            m_SpinUpper  = new CANJaguar(RobotMap.TURRET_SPIN_JAG);
            m_SpinUpper.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        } 
        enable();
        spin(15.0);
        //m_SpinUpper.
    }
    

    
    
    public void initDefaultCommand() {
        //setDefaultCommand(new ShooterSpinnerSetSpeed(10));
    }
  
     
     
    
    protected double returnPIDInput() {
         m_Period = m_RotateSpeed.getPeriod();
         if((m_Period > .005) && (m_Period <.5) && (!Double.isNaN(m_Period))){
         m_scaledProcessVar = 1.0/m_Period;
         }
         updateStatus();
         return m_scaledProcessVar;
     }
     
     
     
    protected void usePIDOutput(double output) {
         try { 
         m_SpinUpper.setX(output);
        //tankDrive(output, output);
         } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }  
    }
    
   
    public void spin (double speed){//use PID control
        if(RobotMap.DEBUG  || RobotMap.SH_DEBUG){System.out.println("Shooter speed : " + speed);}
        
        setSetpoint(speed);
        m_speedReq = speed;
    }
    
    
    public double getShooterSpeed(){
        return  m_scaledProcessVar;
    }
    

    
    public void fireThrust(){       
        m_shootRetractSol.set(false);
        m_shootfireSol.set(true);       
    }
    public void fireRetract(){
         m_shootfireSol.set(false);
         m_shootRetractSol.set(true);       
    }
    public void fireRest(){
         m_shootfireSol.set(false);
         m_shootRetractSol.set(false);       
    }
    
  

    public void updateStatus() {
         if (RobotMap.DEBUG || RobotMap.SH_DEBUG) {
             SmartDashboard.putNumber("Shooter Speed", m_scaledProcessVar);
             SmartDashboard.putNumber( "Shooter Speed Setpoint",m_speedReq);
             SmartDashboard.putNumber( "Shooter Period", 1000.0 * m_Period);
         }
    }
 
    
}
            

 
    

