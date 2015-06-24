package org.firebears.commands;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationEnhancedIO;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.OI;
import org.firebears.RobotMap;
import org.firebears.subsystems.*;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    //public static Chassis2 chassis2 = new Chassis2();
    public static OI oi;
    public static Chassis chassis = new Chassis();
    public static Wedge wedge = new Wedge();
    public static AirCompressor airCompressor =  new AirCompressor();
    public static Camera camera = new Camera();
    public static Conveyer conveyer =  new Conveyer();
    public static Collector collector = new Collector();
    public static Rangefinder rangefinder = null; // new RangeFinder(); 
    public static Turret turret = new Turret();
    public static Shooter shooter = new Shooter();
    public static AnalogChannel m_WallRangeFinderChannel;

    DriverStationEnhancedIO dseio = DriverStation.getInstance().getEnhancedIO();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();
        //m_WallRangeFinderChannel = new AnalogChannel(RobotMap.RANGE_FINDER_CH);
        //m_WallRangeFinder = new RangeFinder(m_WallRangeFinderChannel);
        // Show what command your subsystem is running on the SmartDashboard
        if (RobotMap.DEBUG) {
            if (wedge!=null) SmartDashboard.putData(wedge);
            if (chassis!=null) SmartDashboard.putData(chassis);

            if (camera!=null) SmartDashboard.putData(camera);
            if (airCompressor!=null) SmartDashboard.putData(airCompressor);
            if (conveyer!=null) SmartDashboard.putData(conveyer);

            if (turret!=null) SmartDashboard.putData(turret);

            SmartDashboard.putData(Scheduler.getInstance());
        }
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }
    
    public static void updateStatus() {
        if (chassis != null) chassis.updateStatus(); 
        if (shooter != null) shooter.updateStatus();
        if (camera != null) camera.updateStatus();
        if (turret != null) turret.updateStatus();
    }
}
