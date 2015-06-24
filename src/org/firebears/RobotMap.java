package org.firebears;

import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Encoder;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    // public static final int leftMotor = 1;
    // public static final int rightMotor = 2;
    
    // If you are using multiple modules, make sure to define both the port
    //Encoder rtChassisEncoder;
    
    
//Debug statement enablers, set all to false during contest
    public static final boolean DEBUG = false;
    public static final boolean SH_DEBUG = true;
    public static final boolean TU_DEBUG = true;
    public static final boolean ARM_DEBUG = false;
   
    // Analog I/O channels
    public static final int GYRO_CH = 1;
    public static final int TURRET_POT_CH = 2;
    public static final int RANGE_FINDER_CH = 3;
    
    
    
    //DI I/O channels
    public static final int COMPRES_PS_CH = 1;
    public static final int SPIN_COUNTER_CH = 2;
    public static final int BALL_IN_SHOOTER_CH = 3;
    public static final int BALL_IN_COLLECTOR_CH = 4;
    public static final int RT_ENC_A_CH = 5;   
    public static final int RT_ENC_B_CH = 6; 
    public static final int LFT_ENC_A_CH = 7;    
    public static final int LFT_ENC_B_CH = 8;
    
    
       
    //DO I/O channels
    public static final int COMPRES_OUT_CH = 7;//Just what is the limit here
    
    
       
    //Solenoid I/O channels
    public static final int WEDGE_DEPLOY_CH = 1;
    public static final int WEDGE_RETRACT_CH = 2;
    public static final int SHOOT_FIRE_CH = 3;
    public static final int SHOOT_RETRACT_CH = 4;
    
    //Standard Joystick buttons
    public static final int JOY_DRIVER = 1;//Driver stick
    public static final int JOY_LOAD_SP = 2;//Load specialist stick
    
   /* public static final int JOY_TRIGGER = 1;
    public static final int JOY_TOP_BACK = 2;
    public static final int JOY_TOP_CTR = 3;
    public static final int JOY_TOP_LEFT = 4;//From back of stick
    public static final int JOY_TOP_RIGHT = 5;
    public static final int JOY_BASE_LEFT_F = 6;// left front
    public static final int JOY_BASE_LEFT_R = 7;// left rear
    public static final int JOY_BASE_REAR_LEFT = 8;//Rear left
    public static final int JOY_BASE_REAR_RIGHT = 9;//Rear right
    public static final int JOY_BASE_RIGHT_F = 10;// right front
    public static final int JOY_BASE_RIGHT_R = 11;// right rear
    */
      
    public static final int JOY_WEDGE_DEPLOY_D1 = 1;//Driver
    //public static final int JOY_LO_SPEED = 2;//Driver - Not Used
    public static final int JOY_LINEUP_TEMP_D3 = 3;//Driver - May not need this with PID control
    public static final int JOY_FAST_DRIVE_D6 = 6;
    public static final int JOY_SLOW_DRIVE_D7 = 7;
    
    
    //LOAD SPECIALIST BUTTONS
    public static final int JOY_FIRE_BALL_MANUAL_LS1 = 1;//load specialist - Manual ball solenoid shoot
    public static final int JOY_SHOOT_PWR_DN_LS2 = 2;
    public static final int JOY_SHOOT_PWR_UP_LS3 = 3;//load specialist - Manual change Power (spin speed hi-mid-low)
    public static final int JOY_TAKE_PICTURE_LS4 = 4; // load specialist - take picture, not a game thing
  
    public static final int JOY_TURRET_POT_OFFSET_LS8 = 8;//Used to find POT center after reassembly, not a game thing
    public static final int JOY_TURRET_AUTO_SHOOT_LS9 = 9;   
    public static final int JOY_ELEVATOR_OUT_LS11 = 11;//
    public static final int JOY_ELEVATOR_IN_LS10 = 10;//
    
    
   
    
    // Console extended switches
    public static final int CNSL_SPINNER = 1;//Console extended toggle switch
    public static final int CNSL_COMPRESS_OVERRIDE = 2;//Console extended toggle switch
    
    // Console extended Tracking LED output
    public static final int CNSL_OrRed_NOT_TRACK = 12;
    public static final int CNSL_G_TRACK = 14;
    
    // Console extended Speed LED output
    public static final int CNSL_B_HI_3 = 10; //Blue  
    public static final int CNSL_W_MID_2 = 15;//White
    public static final int CNSL_R_LOW_1 = 16; // red 
    
    
     
    
    //specials
    public static final double CH_SPEED_MIN = 0.3;
        
     
    
    // Keys into the robot Preferences
    public static final String CH_SPEED_MULT_KEY = "chassis.multiplier";
    public static final String CA_SAVE_PICT_KEY = "camera.savePictures";
    public static final String CA_BRIGHTNESS_KEY = "camera.brightness";
    
    public static final String CA_THRESH_HUE_MIN = "camera.HUE.red.min"; // HUE or RED
    public static final String CA_THRESH_HUE_MAX = "camera.HUE.red.max"; // HUE or RED
    public static final String CA_THRESH_SATURATION_MIN = "camera.SAT.green.min"; // SAT or GREEN
    public static final String CA_THRESH_SATURATION_MAX = "camera.SAT.green.max"; // SAT or GREEN
    public static final String CA_THRESH_BRIGHT_MIN = "camera.VAL.blue.min";     // VALUE or BLUE
    public static final String CA_THRESH_BRIGHT_MAX = "camera.VAL.blue.max";     // VALUE or BLUE
    public static final String CA_THRESH_USE_HSV = "camera.thresh.hsv"; // true if HSV thresholds
    
    public static final String TU_CAMERA_OFFSET = "turret.offset";
    
    
    public static final String CAMERA_IP_ADDR = "10.28.46.11";
    
    
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    // 2011 Red robot Jaguar assignments
  
//    public static final int FRONT_LEFT_JAG = 11;
//    public static final int REAR_LEFT_JAG = 2;
//    public static final int FRONT_RIGHT_JAG = 12;
//    public static final int REAR_RIGHT_JAG = 10;
//    
//    public static final int TURRET_ROTATE_JAG = 7;//16  red robot jag 16 goes one way only!!!!!!
//    public static final int TURRET_SPIN_JAG = 16;//7
//    public static final int ELEVATOR_SUCKER_JAG = 14;
//    public static final int CONVEYER_JAG = 13;
    
//    //Upper claw rotor   6
    //Lower claw rotor   7
    //ARM  rotor 1       13
    //ARM  rotor 2       16
    //Deployer           14
    
    
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //Competition 2012 configuration
    public static final int REAR_LEFT_JAG = 1;
    public static final int FRONT_LEFT_JAG = 2;
    public static final int REAR_RIGHT_JAG = 3;
    public static final int FRONT_RIGHT_JAG = 4;   
    public static final int TURRET_SPIN_JAG = 5;
    public static final int TURRET_ROTATE_JAG = 6;    
    public static final int ELEVATOR_SUCKER_JAG = 7;
    public static final int CONVEYER_JAG = 8;


    
    
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //Suitcase Robot Jaguars CAUTION MOTORS CAN BE PLUGGED IN ANY WAY
        //4,5,17,21 are the Jag addresses

//    public static final int FRONT_LEFT_JAG = 4;
//    public static final int REAR_LEFT_JAG = 21;
//    public static final int FRONT_RIGHT_JAG = 5;
//    public static final int REAR_RIGHT_JAG = 17;
//    
//    public static final int TURRET_ROTATE_JAG = 100;
//    public static final int TURRET_SPIN_UPPER_JAG = 100;
//    public static final int TURRET_SPIN_LOWER_JAG = 100;
//    public static final int ELEVATOR_JAG = 100;


    
    

}


