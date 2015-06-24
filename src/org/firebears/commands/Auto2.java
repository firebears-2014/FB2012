/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Turn towards the target and shoot, using Camera.
 * 
 * @author keith
 */
public class Auto2 extends CommandGroup {

    public Auto2() {
        addSequential(new ShooterSpinnerSetSpeed(45.0));
        addSequential(new ConveyerStop());

        addSequential(new ShooterFireThrust(.5));
        addSequential(new ShooterFireRetract(.5));
//        addSequential(new ConveyerFeedTimed(3.0));
         addSequential(new CollectorConvveyorFeedTimed(3.0));  
        addSequential(new ConveyerStop());
        
        addSequential(new ShooterSpinnerSetSpeed(45.0));
        addSequential(new ShooterFireThrust(.5));
        addSequential(new ShooterFireRetract(.5));
        addSequential(new CameraTakePicture(true));

        addSequential(new ShooterSpinnerSetSpeed(15.0));
    }
}
