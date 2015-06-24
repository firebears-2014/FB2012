package org.firebears.commands;

import org.firebears.RobotMap;

/**
 * Reset the camera and take a picture, saving image files into the cRIO.
 * After successfully taking a picture, the list of particles can be retrieved
 * from the {@link Camera}.
 */
public class CameraTakePicture extends CommandBase {
    
    static final int START = 3;
    static final int CAMERA_IS_RESET = 2;
    static final int PICTURE_TAKEN = 1;
    static final int FINISHED = 0;
    
    private int m_state = FINISHED;
    final boolean m_saveFiles;

    public CameraTakePicture(String name, boolean save) {
        super(name);
        if(camera != null)requires(camera);
        m_saveFiles = save;
        setRunWhenDisabled(true);
    }
        
    public CameraTakePicture(boolean save) {
        super();
        if(camera != null)requires(camera);
        m_saveFiles = save;
        setRunWhenDisabled(true);
    }

    protected void initialize() {
        m_state = START;
    }

    /**
     * Perform one state transition, then return control to
     * the Scheduler.
     */
    protected void execute() {
        switch (m_state) {
            
            case START : 
                camera.reset();
                m_state = CAMERA_IS_RESET;
                break;
                
            case CAMERA_IS_RESET :
                camera.takePicture();
                m_state = PICTURE_TAKEN;
                break;
                
            case PICTURE_TAKEN : 
                double dist = 144.0;
                // TODO get distance from range finder
                camera.processImage(dist, m_saveFiles);
                m_state = FINISHED;
                break;
                
            case FINISHED : 
                break;
                
            default :
                if (RobotMap.DEBUG) {System.out.println("unknown state: " + m_state);}
                m_state = FINISHED; 
        }

    }

    protected boolean isFinished() {
        return m_state == FINISHED;
    }

    protected void end() {
    }

    protected void interrupted() {
    }

    public String toString() {
        return "TakePicture[" + m_saveFiles + "]";
    }
}
