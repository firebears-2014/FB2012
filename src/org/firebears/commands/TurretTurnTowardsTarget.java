package org.firebears.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import org.firebears.RobotMap;
import org.firebears.subsystems.Camera;

/**
 * Turn the robot towards a target seen by the camera. If no target is seen,
 * just stop.
 */
public class TurretTurnTowardsTarget extends CommandBase {

    // Machine states
    static final int START = 100;
    static final int CAMERA_RESETTING = 210;
    static final int CAMERA_TAKING_PIC = 220;
    static final int CAMERA_PROCESSING_PIC = 230;
    static final int TURRET_TURNING = 300;
    static final int FINISHED = 900;

    static final double MAX_TURRET_ANGLE = 35.0;
    
    private final double MARGIN = 1.0;
        
    private int m_state = FINISHED;
    private double m_dist = 120.0;
    private double m_turnAngle = 0.0;
    private int m_max_iters = 1000;
    private final boolean m_saveFiles;
    private double m_GoToDegrees;
    private double m_cameraOffset = 0.0;
    
    final public Preferences preferences;
    
    public TurretTurnTowardsTarget() {
        super();
        if (turret != null) requires(turret);
        if (camera !=null) requires(camera);
        m_saveFiles = false;
        preferences = Preferences.getInstance();
    }

    protected void initialize() {
        m_state = START;
        m_max_iters = 1000;
        m_cameraOffset = preferences.getDouble(RobotMap.TU_CAMERA_OFFSET, 0.0);
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
                if (m_turnAngle > MAX_TURRET_ANGLE || m_turnAngle < -1 * MAX_TURRET_ANGLE)  {
                    if (RobotMap.DEBUG) {System.out.println("::: cant turn to angle " + m_turnAngle);}
                    m_state = FINISHED;
                    break;
                }
                m_GoToDegrees = turret.getTurretAngle() + m_turnAngle + m_cameraOffset;
                if (m_cameraOffset != 0.0)  System.out.println("::: turret.offset=" + m_cameraOffset);
                m_state = TURRET_TURNING;
                break;

            case TURRET_TURNING:
                angleDiff = turret.getTurretAngle() - m_GoToDegrees;
                if (Math.abs(angleDiff) < MARGIN) {
                    m_state = FINISHED;
                    if (RobotMap.DEBUG) System.out.println("::: " + this + " angle diff = " + angleDiff);
                } else {
                    if (RobotMap.DEBUG) System.out.println("::: angleDiff=" + angleDiff);
                 turret.rotateTurret( m_GoToDegrees);   
                }
                break;

            case FINISHED:
                break;

            default:
                System.out.println("unknown state: " + m_state);
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
 
    }

    protected void interrupted() {
    }
    
    public String toString() {
        return "TurretTurnTowardsTarget[]";
    }
    
    
    
}
