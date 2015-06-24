package org.firebears.commands;

import edu.wpi.first.wpilibj.Preferences;
import java.util.Enumeration;
import java.util.Vector;
import org.firebears.RobotMap;

/**
 * Clean out all existing preferences and rewrite them.
 * Note that the {@link Preferences} javadoc warns against frequent saves of
 * preferences, since it can eventually degrade the memory of the cRIO.
 * This command should only be called ocassionally to clear out unneeded prefs.
 */
public class CleanPreferences extends CommandBase {
    
    final public Preferences preferences;
    
    public CleanPreferences(String name) {
        super(name);
        preferences = Preferences.getInstance();
    }
    
    public CleanPreferences() {
        super();
        preferences = Preferences.getInstance();
    }

    protected void initialize() {
    }

   
    protected void execute() {
        Vector keyList = preferences.getKeys();
        Vector removeList = new Vector();
        for (Enumeration e=keyList.elements(); e.hasMoreElements(); ) {
            String key = (String)e.nextElement();
            if (key.equals(RobotMap.CH_SPEED_MULT_KEY)) continue;
            if (key.equals(RobotMap.CA_SAVE_PICT_KEY)) continue;
            if (key.equals(RobotMap.CA_BRIGHTNESS_KEY)) continue;
            if (key.equals(RobotMap.CA_THRESH_HUE_MIN)) continue;
            if (key.equals(RobotMap.CA_THRESH_HUE_MAX)) continue;
            if (key.equals(RobotMap.CA_THRESH_SATURATION_MIN)) continue;
            if (key.equals(RobotMap.CA_THRESH_SATURATION_MAX)) continue;
            if (key.equals(RobotMap.CA_THRESH_BRIGHT_MIN)) continue;
            if (key.equals(RobotMap.CA_THRESH_BRIGHT_MAX)) continue;
            if (key.equals(RobotMap.CA_THRESH_USE_HSV)) continue;
            if (key.equals(RobotMap.TU_CAMERA_OFFSET)) continue;
            removeList.addElement(key);
        }
        if (removeList.isEmpty())  {
            if (RobotMap.DEBUG) {System.out.println("CleanPreferencs: no keys to remove");}
        } else {
            for (Enumeration e=removeList.elements(); e.hasMoreElements(); ) {
                String key = (String)e.nextElement();
                preferences.remove(key);
                if (RobotMap.DEBUG) {System.out.println("CleanPreferencs: removing " + key);}
            }
            preferences.save();
        }
        
    }

    protected boolean isFinished() {
        return true;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
