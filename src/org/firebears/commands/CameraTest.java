package org.firebears.commands;

/**
 * Test processing of an image file on the cRIO.
 */
public class CameraTest extends CommandBase {
    
    final String m_filename;
    final boolean m_saveFiles;
    
    public CameraTest(String name, String fname, boolean save) {
        super(name);
        m_filename = fname;
        m_saveFiles = save;
    }

    protected void initialize() {
    }

    protected void execute() {
        camera.reset();
        boolean successful = camera.loadPicture(m_filename);
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
