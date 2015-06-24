/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.subsystems;


import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.smartdashboard.SendableGyro;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.RobotMap;

/**
 *
 * @author Paul
 */
public class Chassis2 extends Subsystem {
    
    final public Preferences preferences;
    
    //Initializes the motors
    private CANJaguar frontLeft;
    private CANJaguar frontRight;
    private CANJaguar rearLeft;
    private CANJaguar rearRight;
    
    //Initializes the encoders
    private Encoder leftEncoder;
    private Encoder rightEncoder;
    
    //Initializes the drive
    private RobotDrive drive;
    
    //Initializes the PID Controllers
    private PIDController leftFrontPID;
    private PIDController rightFrontPID;
    private PIDController leftRearPID;
    private PIDController rightRearPID;
        
    protected Gyro gyro;
        
    public static final double Kp = .3;//TODO Tune Chassis PID constants
    public static final double Ki = .01;
    public static final double Kd = 0.0;
    public static final double PID_Y_SPEED_SCALE = 100.0;
    
    public Chassis2() {

        super();
        preferences = Preferences.getInstance();
        if (!preferences.containsKey(RobotMap.CH_SPEED_MULT_KEY)) {
            preferences.putDouble(RobotMap.CH_SPEED_MULT_KEY, 1.0);
            preferences.save();
        }

        // Allocate Jags
        try {
            frontLeft = new CANJaguar(RobotMap.FRONT_LEFT_JAG);
            frontRight = new CANJaguar(RobotMap.FRONT_RIGHT_JAG);
            rearLeft = new CANJaguar(RobotMap.REAR_LEFT_JAG);
            rearRight = new CANJaguar(RobotMap.REAR_RIGHT_JAG);
            drive = new RobotDrive(frontLeft, frontRight, rearLeft, rearRight);
        } catch (CANTimeoutException e) {
            e.printStackTrace();
        }

        //Starts the encoders
        leftEncoder = new Encoder(RobotMap.LFT_ENC_A_CH, RobotMap.LFT_ENC_B_CH);
        rightEncoder = new Encoder(RobotMap.RT_ENC_A_CH, RobotMap.RT_ENC_B_CH);
        leftEncoder.start();
        rightEncoder.start();

        //Initializes the PID Controllers
        leftFrontPID = new PIDController(Kp, Ki, Kd, leftEncoder, frontLeft);
        rightFrontPID = new PIDController(Kp, Ki, Kd, rightEncoder, frontRight);
        leftRearPID = new PIDController(Kp, Ki, Kd, leftEncoder, rearLeft);
        rightRearPID = new PIDController(Kp, Ki, Kd, rightEncoder, rearRight);

        //Enables the PID Controllers.
        leftFrontPID.enable();
        rightFrontPID.enable();
        leftRearPID.enable();
        rightRearPID.enable();

        //Sets distance in inches
        //This was obtained by finding the circumfrance of the wheels,
        //finding the number of encoder pulses per rotation,
        //and diving the circumfrance by that number.
        double leftSpd = leftEncoder.getRate();//TODO Tune scale speed from Encoder rate to -1 to +1 
        double rightSpd = rightEncoder.getRate();

        if (RobotMap.DEBUG) {
            SmartDashboard.putData("Left front wheel", leftFrontPID);
            SmartDashboard.putData("Right front wheel", rightFrontPID);
            SmartDashboard.putData("Left rear wheel", leftRearPID);
            SmartDashboard.putData("Right rear wheel", rightRearPID);
            SmartDashboard.putData("Gyro", gyro);
        }
        
        gyro = new Gyro(RobotMap.GYRO_CH);
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {        
    }
    
    public void arcadeDrive(GenericHID stick) {
        //double m = preferences.getDouble(RobotMap.CH_SPEED_MULT_KEY, 1.0);
        double moveValue = (stick.getY()); //* stick.getY() * (stick.getY() < 0.0 ? -1 : 1);
        double rotateValue = stick.getX();
        // local variables to hold the computed PWM values for the motors
        moveRotateToXY(moveValue, rotateValue);
    }

    protected void initDefaultCommand() {
    }
    
    public void straight(double speed){
       leftFrontPID.setSetpoint(speed);
       leftRearPID.setSetpoint(speed);
       rightFrontPID.setSetpoint(speed);
       rightRearPID.setSetpoint(speed);
    }
    
    public void turnSlowly(boolean clockwise) {
        
        moveRotateToXY(0.0,clockwise ? -0.3 : 0.3);
    }
    
    public void stop() {
        drive.arcadeDrive(0.0, 0.0);
    }
    
    private void moveRotateToXY(double moveValue, double rotateValue){
        double leftMotorSpeed;
        double rightMotorSpeed;
        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        leftFrontPID.setSetpoint(leftMotorSpeed);
        leftRearPID.setSetpoint(leftMotorSpeed);
        rightFrontPID.setSetpoint(rightMotorSpeed);
        rightRearPID.setSetpoint(rightMotorSpeed);
    }
    
    public void updateStatus() {
        if (RobotMap.DEBUG) {
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
