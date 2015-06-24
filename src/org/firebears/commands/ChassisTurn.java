package org.firebears.commands;

//import edu.wpi.first.wpilibj.command.subsystem.;
//import edu.wpi.first.wpilibj.command.PIDSubsystem;
//import edu.wpi.first.wpilibj.networktables.NetworkTable;
//import edu.wpi.first.wpilibj.smartdashboard.PIDController;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
//import edu.wpi.first.wpilibj.networktables.NetworkTableKeyNotDefined;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.RobotMap;

/**
 * Turn by a specific angle, using the gyro and a PIDController.
 * When initiated, this command will pick a target angle.  The robot will
 * then rotate until the difference between the gryo angle and the target
 * angle are close, within a margin of one degree.
 */
public class ChassisTurn extends PIDCommand {

    private double m_turnAngle;
    private double m_targetAngle;
    public static final double P = .015;//TODO Tune this Chassis turn PID??
    public static final double I = .001;
    public static final double D = 0.0;
    public final double MARGIN = 1.0;
    private int m_max_iters = 1000;

    public ChassisTurn(String name, double angle) {
        super(name, P, I, D);
		initCommand(angle);
    } 

    public ChassisTurn(double angle) {
        super(P, I, D);
		initCommand(angle);
    }
	
	private void initCommand(double angle) {
		m_turnAngle = angle;
                if (CommandBase.chassis!=null) requires(CommandBase.chassis);
		getPIDController().setInputRange(-180.0d, 180.0d);
                /*if (RobotMap.DEBUG) {
                    boolean onDashboard = false;
                    try {
                            if (SmartDashboard.getData("ChassisTurn PID") != null)
                                    onDashboard = true;
                    } catch (NetworkTableKeyNotDefined e) {
                            onDashboard = false;
                    }
                    if (!onDashboard) {
                            SmartDashboard.putData("ChassisTurn PID",
                                            (PIDController) this.getPIDController());
                    }
                }*/
        }
	

    /**
     * Set the setpoint to zero degrees difference.
     * Set the target angle.
     */
    protected void initialize() {
        m_max_iters = 1000;
        setSetpoint(0.0);
        m_targetAngle = CommandBase.chassis.fixAngle(CommandBase.chassis.getGyroAngle() + m_turnAngle, 360);
        if (RobotMap.DEBUG) {System.out.println("::: chassisTurn.initialize: " + m_targetAngle);}
    }

    /**
     * Finish when the relative angle falls below the margin of error.
     * Also, finish if we go through 1000 iterations without finding the angle.
     */
    protected boolean isFinished() {
        if (m_max_iters <= 0) { return true; }
        return Math.abs(returnPIDInput()) <= MARGIN;
    }

    protected void end() {
        CommandBase.chassis.stop();
        if (RobotMap.DEBUG) {System.out.println("::: chassisTurn.end(): " + this.returnPIDInput());}
    }

    protected void interrupted() {
        CommandBase.chassis.stop();
        if (RobotMap.DEBUG) {System.out.println("::: chassisTurn.interrupt(): " + this.returnPIDInput());}
    }

    protected void execute() {
        m_max_iters--;
        if (RobotMap.DEBUG) {System.out.println("::: chassisTurn.execute(): " + this.returnPIDInput());}
    }

    /**
     * @return an angle in the range -180 to 180 degrees.
     */
    protected double returnPIDInput() {
        return CommandBase.chassis.getGyroAngleTo(m_targetAngle);
    }

    /**
     * @param output value between -1.0 and 1.0.
     */
    protected void usePIDOutput(double output) {
        CommandBase.chassis.turn(output);
    }

    
}
