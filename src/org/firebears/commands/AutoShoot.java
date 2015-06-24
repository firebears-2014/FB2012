package org.firebears.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.PrintCommand;
import org.firebears.subsystems.Camera;

/**
 * Take a picture with the {@link Camera} and shoot based on a particle found.
 */
public class AutoShoot extends CommandGroup {
    
    public AutoShoot() {

        addSequential(new TurretTurnTowardsTarget());  // Takes a pictue
        addSequential(new ShooterSpinnerSetSpeedFromCamera(this));
//        addSequential(new ElevatorStop());
                
        addSequential(new PrintCommand("Shooting now"));
        addSequential(new ShooterFireThrust(.5));
        addSequential(new ShooterFireRetract(.5));
        
        addSequential(new PrintCommand("Reseting Turret"));
        addSequential(new ShooterSpinnerSetSpeed(15.0));
        addSequential(new TurretCenter());

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
}
