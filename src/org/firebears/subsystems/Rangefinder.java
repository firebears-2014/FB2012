/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.firebears.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.firebears.RobotMap;

/**
 *
 * @author Javad
 */
public class Rangefinder {
   
    double m_inches;
    AnalogChannel m_input;
    final double VOLT_DIST_RATIO = 0.00929687; //5.084 Volts / 512 inch range 0.009929687
    final int AVERAGE_BITS = 5;

    /**
     * Constructor.  Takes only an argument specifying analog channel to use
     *
     * @param achannel analog channel to use
     */
    public Rangefinder() {
        m_input = new AnalogChannel(RobotMap.RANGE_FINDER_CH);
        m_input.setAverageBits(AVERAGE_BITS); //this number can be increased for better, slower readings
    }//

    /**
     * Gets voltage returned by the Ultrasonic sensor
     *
     * @return the voltage
     */
    public double getVoltage() {
        double V_Raw = m_input.getAverageVoltage();
        double V_Filtered = Filter(V_Raw);
        if (RobotMap.DEBUG) {
            SmartDashboard.putNumber("V_Raw ", V_Raw); 
            SmartDashboard.putNumber("V_Filtered ", V_Filtered); 
        }
        return V_Filtered;
    }

    /**
     * Gets the sensor's distance from the nearest object.
     * Note that this is not necessarily the intended object!
     *
     * @return the distance in inches
     */
    public double getDistance() {
        double Distance = getVoltage() / VOLT_DIST_RATIO;
        if (RobotMap.DEBUG) SmartDashboard.putNumber("Distance ", Distance);
        return Distance;
    }
    
    
    //FILTER***************************
    //Sonic range finder blurts out sproadic max values when distance beyond .7V
    //This is a progressive EMA filter, where tuning slows changes with increasing volts
    
    double m_VOut;
    boolean m_firstTime = true;

    private double Filter(double Volts){
 
        if(m_firstTime){//Initialize movong average
            m_firstTime = false;
            m_VOut = Volts;
        }
            if(Volts <= 1.0  ) {
                m_VOut = m_VOut + .2 * (Volts - m_VOut);
                //m_VOut = Volts;
            return m_VOut;
        }
            else if(Volts <= 1.5 ) {
                m_VOut = m_VOut + .2 * (Volts - m_VOut);
                //m_VOut = Volts;
            return m_VOut;
        }
            else if( Volts <= 2 ) {
                m_VOut = m_VOut + .1 * (Volts - m_VOut);
                //m_VOut = Volts;
            return m_VOut;
        }
            else if(Volts <= 2.4 ) {
                m_VOut = m_VOut + .1 * (Volts - m_VOut);
                //m_VOut = Volts;
            return m_VOut;
        }            
             else{//Beyond  V limit,  no change
            return (m_VOut); 
        }
    }
}

