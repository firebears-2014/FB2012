package org.firebears.subsystems;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.firebears.RobotMap;
import org.firebears.commands.ChassisDriveJoysticks;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Chassis and wheels.
 */
public class Chassis  extends Subsystem {

    CANJaguar frontLeft, rearLeft, frontRight, rearRight;
    RobotDrive drive;
    protected Gyro gyro;
    
    final public Preferences preferences;
    
    public static final double P = .1;
    public static final double I = .01;
    public static final double D = 0.0;
    public static final double PID_Y_SPEED_SCALE = 100.0;
    
    private final Encoder leftEncoder = new Encoder(RobotMap.LFT_ENC_A_CH,RobotMap.LFT_ENC_B_CH);
    private final Encoder rightEncoder = new Encoder(RobotMap.RT_ENC_A_CH,RobotMap.RT_ENC_B_CH);
    
 
    public Chassis () {
        super();
        preferences = Preferences.getInstance();
        leftEncoder.start();
        rightEncoder.start();
        gyro = new Gyro(RobotMap.GYRO_CH);
        
        if (RobotMap.DEBUG) SmartDashboard.putData("Gyro", gyro);
        
        if (! preferences.containsKey(RobotMap.CH_SPEED_MULT_KEY)) {
            preferences.putDouble(RobotMap.CH_SPEED_MULT_KEY, 1.0);
            preferences.save();
        }

        try {
            frontRight = new CANJaguar(RobotMap.FRONT_RIGHT_JAG); 
            rearRight = new CANJaguar(RobotMap.REAR_RIGHT_JAG); 
            frontLeft = new CANJaguar(RobotMap.FRONT_LEFT_JAG); 
            rearLeft = new CANJaguar(RobotMap.REAR_LEFT_JAG); 
            drive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
            drive.setExpiration(1.0);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
 

    }

    public void initDefaultCommand() {
        setDefaultCommand(new ChassisDriveJoysticks());
    }
    
 
    
    
    public void arcadeDrive(GenericHID stick) {
        double m = preferences.getDouble(RobotMap.CH_SPEED_MULT_KEY, 1.0);
        double moveValue = f(stick.getY()); //* stick.getY() * (stick.getY() < 0.0 ? -1 : 1);
        double rotateValue = stick.getX();
        drive.arcadeDrive(-1 * m * moveValue, -1 * m * rotateValue, true);  
    }
          
    private double f(double x){
        if(x < 0.05 && x > -0.05){
            return 0.0;
        }
        else if(x > 0.05){
            return RobotMap.CH_SPEED_MIN + 1.85 * RobotMap.CH_SPEED_MIN * x;
        }
        else{
            return - RobotMap.CH_SPEED_MIN + 1.85 * RobotMap.CH_SPEED_MIN * x;
        }
    }
    
    public void tankDrive(GenericHID leftStick, GenericHID rightStick) {
        double m = 0.6;
        drive.tankDrive(leftStick, rightStick);
    }
    
    public void turnSlowly(boolean clockwise) {
        double rotateValue = (clockwise ? -0.6 : 0.6);
        drive.arcadeDrive(0.0, rotateValue);
    }
    
    public void turn(double x) {
        drive.arcadeDrive(0.0, x);
    }
    
    public void stop() {
        drive.arcadeDrive(0.0, 0.0);
    }
    
     public void straight(double speed){
       //SmartDashboard.putDouble("Wall Dist", CommandBase.m_WallDistIn.getDistance());
       
       drive.arcadeDrive(speed,0.0);
   }
     
   public void updateStatus() {
       if (RobotMap.DEBUG)  {
            SmartDashboard.putNumber("Left encoder", leftEncoder.getRate());
            SmartDashboard.putNumber("Right encoder", rightEncoder.getRate());
            if (gyro != null) SmartDashboard.putNumber("Gyro angle",gyro.getAngle());
       }
    }
   
   /**
    * Get the current gyroscope angle.
    * 
    * @return an angle between 0 and 360 degrees.
    */
   public double getGyroAngle() {
       return fixAngle(gyro.getAngle(), 360);
   }
   
   /**
    * Get the relative angle from the current gyroscope angle to the 
    * target angle.
    * 
    * @param targetAngle angle between 0 and 360 degrees.
    * @return an angle between -180 and 180 degrees.
    */
   public double getGyroAngleTo(double targetAngle) {
       double currentAngle = getGyroAngle();
       double angle = targetAngle - currentAngle;
       if (angle > 180) {
           angle -= 360;
       } else if (angle < -180){
           angle += 360;
       }
       return fixAngle(angle, 180);
   }
   
    /**
     * Wrap an angle to a range of 360 degrees.
     * The gyro may return values outside the range -360 to 360.
     * This method fixes all angles into a reasonable range.
     */
    public double fixAngle(double a, double maxAngle) {
        double angle = a;
        double minAngle = maxAngle - 360;
        while (angle<minAngle || angle>maxAngle) {
            if (angle > maxAngle) {
                angle -= 360;
            } else if (angle < minAngle) {
                angle += 360;
            } else {
                break;
            }
        } 
        return angle;
    }
    
    
}
