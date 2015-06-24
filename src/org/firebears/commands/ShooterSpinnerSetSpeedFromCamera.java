package org.firebears.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import org.firebears.RobotMap;
import org.firebears.subsystems.Camera;

/**
 * Set the spinner speed based on the distance calculated from the 
 * {@link Camera}.    If no picture is found, we can cancel the parent
 * {@link CommandGroup}.
 */
public class ShooterSpinnerSetSpeedFromCamera extends CommandBase {

    //Shooter speed state
    private double m_speed;
    private double m_dist = 0.0;
    private CommandGroup m_parent_commandGroup = null;
    private ParticleAnalysisReport m_particle;
    private double m_turnAngle=0.0;

    public ShooterSpinnerSetSpeedFromCamera(String name) {
        super(name);
        if (shooter != null) requires(shooter);
    }

    public ShooterSpinnerSetSpeedFromCamera() {
        super();
        if (shooter != null) requires(shooter);
    }

    
    public ShooterSpinnerSetSpeedFromCamera(CommandGroup parentCG) {
        super();
        if (shooter != null) requires(shooter);
        m_parent_commandGroup = parentCG;
    }
    
    protected void initialize() {
        m_particle = camera.getBackboard();
        if (m_particle != null) {
            m_dist = Camera.estimateDistance(m_particle);
            m_turnAngle = Camera.estimateAngle(m_particle, m_dist);
            m_speed = calculateSpinnerSpeed(m_dist, m_turnAngle);
            if (RobotMap.DEBUG) System.out.println(this + " ::: m_dist=" + m_dist + " : m_speed=" + m_speed);
            setTimeout(5.0);
        } else {
            m_dist = 0;
            m_speed = 15.0;
            shooter.spin(m_speed);
            if (m_parent_commandGroup!=null) m_parent_commandGroup.cancel();
            if (RobotMap.DEBUG) System.out.println(this + " ::: No picture in camera");
        }
    }

    protected void execute() {
        if (RobotMap.DEBUG || RobotMap.SH_DEBUG) {
            System.out.println("Spinner Command : " + m_speed);
        }
        shooter.spin(m_speed);
    }

    protected boolean isFinished() {
        if (m_particle == null) { 
            return true; 
        }
        if (Math.abs(shooter.getShooterSpeed() - m_speed) / m_speed < .02) {
            return true;
        } else {
            return false;
        }
    }

    protected void end() {
    }

    protected void interrupted() {
    }
    
    public String toString() {
        return "ShooterSpinnerSetSpeedFromCamera[" + m_parent_commandGroup + "]";
    }



    /**
     * @param inches distance in inches.
     * @return rotational speed in revolutions per second.
     */
    protected double calculateSpinnerSpeed(double inches, double angle) {
        double k =  22; // 20.0; // 18.5;// 18.1838
        double slope = 0.118693;
        double sq = 0.0001;
        double k2=2/30;
        double rps = k + slope * inches + inches * inches * sq + k2*Math.abs(angle);
        if (rps > 55)  rps = 55;
        return rps;
    }


}