/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author paul
 */
public class Auto extends CommandGroup {
    
    public Auto() {
        //addSequential(new TurretCenter());
        
//        addSequential(new CameraTakePicture(true));
        
        addSequential(new TurretTurnTowardsTarget());  // Takes a pictue
        addSequential(new ShooterSpinnerSetSpeedFromCamera(this));
        addSequential(new ConveyerStop());
        addSequential(new CollectorStop());
        
        addSequential(new ShooterFireThrust(.5));
        addSequential(new ShooterFireRetract(.5));
        
        addSequential(new CollectorConvveyorFeedTimed(3.5));
        addSequential(new ConveyerStop());
        addSequential(new CollectorStop());
        
        //addSequential(new ShooterSpinnerSetSpeed(45.0));
        addSequential(new ShooterFireThrust(.5));
        addSequential(new ShooterFireRetract(.5));
//        addSequential(new CameraTakePicture(true));

    }
    
    /**
     * Called if this command is canceled.
     */
    protected void interrupted() {
        super.interrupted();
        CommandBase.shooter.spin(15.0);
        CommandBase.turret.rotateTurret(0.0);
        CommandBase.shooter.fireRetract();
    }
    
    /**
     * Called if this command is finished.
     */
    protected void end() {
        super.end();
        CommandBase.shooter.spin(15.0);
        CommandBase.turret.rotateTurret(0.0);
        CommandBase.shooter.fireRetract();
    }
}
