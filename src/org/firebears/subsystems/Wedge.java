package org.firebears.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.firebears.RobotMap;
import org.firebears.commands.WedgeRetract;

/**
 *
 * @author BEN!!!!!!!!!!!!!!!!11!!!!!!!!!!!!!!!!
 */
public class Wedge extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.\\
    Solenoid m_WedgeDeploy;
    Solenoid m_WedgeRetract;
    
    public Wedge(){
    m_WedgeDeploy = new Solenoid(RobotMap.WEDGE_DEPLOY_CH);
    m_WedgeRetract = new Solenoid(RobotMap.WEDGE_RETRACT_CH);
    }
     public void initDefaultCommand() {
        setDefaultCommand(new WedgeRetract());
    }

    public boolean wedgeRetract() {
       if(RobotMap.ARM_DEBUG){System.out.println("Retract Arm : " + m_WedgeDeploy);}
        m_WedgeDeploy.set(false);
        m_WedgeRetract.set(true);
        boolean retracted = true;
        return retracted;
    }

    public boolean wedgeDeploy() {
        if(RobotMap.ARM_DEBUG){System.out.println("Deploy Arm");}
        m_WedgeRetract.set(false);
        m_WedgeDeploy.set(true);
        boolean deployed = true;
        return deployed;
    }
    
    public boolean wedgeRest() {
        if(RobotMap.ARM_DEBUG){System.out.println("Deploy Arm");}
        m_WedgeRetract.set(false);
        m_WedgeDeploy.set(false);
        boolean deployed = true;
        return deployed;
    }
    
    
}