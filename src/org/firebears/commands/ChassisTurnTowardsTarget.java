package org.firebears.commands;

import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import org.firebears.RobotMap;
import org.firebears.subsystems.Camera;

/**
 * Turn the robot towards a target seen by the camera. If no target is seen,
 * just stop.
 */
public class ChassisTurnTowardsTarget extends CommandBase {

    // Machine states
    static final int START = 100;
    static final int CAMERA_RESETTING = 210;
    static final int CAMERA_TAKING_PIC = 220;
    static final int CAMERA_PROCESSING_PIC = 230;
    static final int CHASSIS_TURNING = 300;
    static final int FINISHED = 900;
    
    private final double MARGIN = 2.0;
        
    private int m_state = FINISHED;
    private double m_dist = 120.0;
    private double m_turnAngle = 0.0;
    private int m_max_iters = 1000;
    private final boolean m_saveFiles;
    private double m_targetAngle;
    
    public ChassisTurnTowardsTarget() {
        super();
        if(chassis != null)requires(chassis);
        if(camera !=null) requires(camera);
        m_saveFiles = false;
    }

    protected void initialize() {
        m_state = START;
        m_max_iters = 1000;
    }

    protected void execute() {
        long time = System.currentTimeMillis();
        int beginning_state = m_state;
        double angleDiff = 0.0, currentAngle = 0.0;
        
        m_max_iters--;
        switch (m_state) {

            case START:
                m_state = CAMERA_RESETTING;
                break;

            case CAMERA_RESETTING:
                camera.reset();
                m_state = CAMERA_TAKING_PIC;
                break;

            case CAMERA_TAKING_PIC:
                camera.takePicture();
                m_state = CAMERA_PROCESSING_PIC;
                break;

            case CAMERA_PROCESSING_PIC:
                m_dist = 120.0;  // TODO get distance from range finder
                camera.processImage(m_dist, m_saveFiles);
                ParticleAnalysisReport target = CommandBase.camera.getBackboard();
                if (target == null) {
                    if (RobotMap.DEBUG) {System.out.println("::: no target found");}
                    m_state = FINISHED;
                    break;
                }
                m_turnAngle = Camera.estimateAngle(target, m_dist);
                currentAngle = chassis.getGyroAngle();
                m_targetAngle = chassis.fixAngle(currentAngle - m_turnAngle, 360);
                m_state = CHASSIS_TURNING;
                break;

            case CHASSIS_TURNING:
                angleDiff = chassis.getGyroAngleTo(m_targetAngle);
                if (Math.abs(angleDiff) < MARGIN) {
                    m_state = FINISHED;
                    chassis.stop();
                } else {
                    boolean clockwise = angleDiff > 0;
                    chassis.turnSlowly(clockwise);
                    if (RobotMap.DEBUG) {System.out.println("::: " + this + " : currenAngle=" 
                            + currentAngle + " : clockwise=" + clockwise);}
                }
                break;

            case FINISHED:
                break;

            default:
                if (RobotMap.DEBUG) {System.out.println("unknown state: " + m_state);}
                m_state = FINISHED;
        }
        time = System.currentTimeMillis() - time;
        if (RobotMap.DEBUG) {System.out.println("::: TurnTowardsTarget(" + time + " ms): state="
                + beginning_state + " to " + m_state);}
    }

    protected boolean isFinished() {
        if (m_max_iters <= 0) {
            return true;
        }
        return m_state == FINISHED;
    }

    protected void end() {
        chassis.stop();
    }

    protected void interrupted() {
        chassis.stop();
    }
    
    public String toString() {
        return "ChassisTurnTowardsTarget[]";
    }
    
}
