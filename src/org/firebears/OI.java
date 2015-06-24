package org.firebears;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.DigitalIOButton;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.commands.*;

    public class OI{
    
    
    
    //Driver Joystick and Buttons
    public Joystick joystickDr = new Joystick(RobotMap.JOY_DRIVER);
    public Button btnDrWedgeDeployD1 = new JoystickButton(joystickDr, RobotMap.JOY_WEDGE_DEPLOY_D1);//1 trigger
    public Button btnDrLineUpD3 = new JoystickButton(joystickDr, RobotMap.JOY_LINEUP_TEMP_D3);//3
    public Button btnFastDriveD6 = new JoystickButton(joystickDr, RobotMap.JOY_FAST_DRIVE_D6);//6
    public Button btnSlowDriveD7 = new JoystickButton(joystickDr, RobotMap.JOY_SLOW_DRIVE_D7);//7
   
    
    //Load Specialist Joystick 
    public Joystick joystickLS = new Joystick(RobotMap.JOY_LOAD_SP);
    
    //Load Specialist joystick buttons   
    public Button btnManualFireBallLS1 = new JoystickButton(joystickLS,RobotMap.JOY_FIRE_BALL_MANUAL_LS1);//1 trigger    
    public Button btnShootSpdDnLS2 = new JoystickButton(joystickLS, RobotMap.JOY_SHOOT_PWR_DN_LS2);
    public Button btnShootSpdUpLS3 = new JoystickButton(joystickLS, RobotMap.JOY_SHOOT_PWR_UP_LS3);
    public Button btnCameraClickLS4 = new JoystickButton(joystickLS, RobotMap.JOY_TAKE_PICTURE_LS4);//4 - Camera button
    public Button btnDisplayPotOffsetLS8 = new JoystickButton(joystickLS, RobotMap.JOY_TURRET_POT_OFFSET_LS8);//8
    public Button btnAutoShootLS9 = new JoystickButton(joystickLS, RobotMap.JOY_TURRET_AUTO_SHOOT_LS9);//9  not used except for maintenance
    public Button btnBallFeedLS10 = new JoystickButton(joystickLS,RobotMap.JOY_ELEVATOR_IN_LS10);//10
    public Button btnCollectorEjectLS11 = new JoystickButton(joystickLS, RobotMap.JOY_ELEVATOR_OUT_LS11);//11
    
    
    //public Button conSwCompDis = new JoystickButton(joystickLS, 8);//8
    //public Button button9 = new JoystickButton(joystickLS, 9);//9
            
    // Console extended LED output
    
    
//    public static final int CNSL_OrRed_NOT_TRACK = 3;
//    public static final int CNSL_G_TRACK = 4;
//    public static final int CNSL_R_LOW_1= 5;
//    public static final int CNSL_B_HI_3= 6;
//    public static final int CNSL_W_MID_2= 7;
   
    
    
    //Enhanced console toggle switches
    //DriverStationEnhancedIO m_eIO;
    DriverStationEnhancedIO m_eIO = DriverStation.getInstance().getEnhancedIO();
   
    public static final Button btnConsoleDisableCompressor = new DigitalIOButton(RobotMap.CNSL_COMPRESS_OVERRIDE);
    public static final Button btnDisableSpinner = new DigitalIOButton(RobotMap.CNSL_SPINNER);
    
    //Special buttons
     public SendableChooser speedChooser = new SendableChooser();
     
     
     
    
   

    public OI() {
        
       //m_eIO.getDigital(channel)
        
        //Driver Joystick commands
        btnDrWedgeDeployD1.whileHeld(new WedgeDeploy());//1 trigger
        btnDrLineUpD3.whenPressed(new ChassisTurnTowardsTarget());
        btnFastDriveD6.whenPressed(new ChassisSetSpeed(1.0));//6
        btnSlowDriveD7.whenPressed(new ChassisSetSpeed(0.6));//7      
          
        
        
         //Load Specialist joystick buttons
        btnManualFireBallLS1.whenPressed(new ShooterFireSeq());//1
        btnShootSpdDnLS2.whenPressed(new ShooterSpinnerSetSpeed("Dn"));//2
        btnShootSpdUpLS3.whenPressed(new ShooterSpinnerSetSpeed("Up"));//3
        btnCameraClickLS4.whenPressed(new CameraTakePicture("CameraTakePicture", true)); // 4
        btnDisplayPotOffsetLS8.whenPressed(new TurretDisplayPotOffset());      
        btnAutoShootLS9.whenPressed(new AutoShoot());//9       
        btnBallFeedLS10.whenPressed(new ConveyerFeed());//11
        btnBallFeedLS10.whenPressed(new CollectorFeed());//11
        btnCollectorEjectLS11.whenPressed(new CollectorEject());//12
             

         //Enhanced console toggle switches
        btnConsoleDisableCompressor.whileHeld(new AirCompressorDisable());
        btnDisableSpinner.whileHeld(new ShooterSpinnerSetSpeed("Idle"));
        
        
        
        //Special buttons
        if (RobotMap.DEBUG) {

            SmartDashboard.putData(new CameraTakePicture("CameraTakePicture", true));
            SmartDashboard.putData(new ChassisTurnByAngle("Turn left 90", 90));
            SmartDashboard.putData(new ChassisTurnByAngle("Turn left 5", 5));
            SmartDashboard.putData(new ChassisTurnByAngle("Turn right 45", -45));
            SmartDashboard.putData(new CleanPreferences("Clean prefs"));
//        SmartDashboard.putData(new ChassisTurn("PID Turn left 90", 90));
//        SmartDashboard.putData(new ChassisTurn("PID Turn right 45", -45));
            for (int i = 5; i <= 50; i += 5) {
                speedChooser.addObject(i + " RPS", Integer.valueOf(i));
            }
            SmartDashboard.putData("Shooter Speed", speedChooser);
        }
        
    }
       

    
}