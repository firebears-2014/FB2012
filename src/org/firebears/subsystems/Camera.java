package org.firebears.subsystems;

import java.io.IOException;
import java.io.PrintStream;

import javax.microedition.io.Connector;

import org.firebears.RobotMap;

import com.sun.squawk.microedition.io.FileConnection;
import com.sun.squawk.util.Arrays;
import com.sun.squawk.util.Comparer;
import com.sun.squawk.util.MathUtils;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.RGBImage;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Subcommand around the Axis camera.
 */
public class Camera extends Subsystem {


    public static final double RECT_MARGIN = 0.80;
    public static final double AREA_MARGIN = 0.50;
    public static final double DISTANCE_FUDGE = 0.88;
    /**
     * Vertical tangent of half the vertical viewing angle.
     * At any distance this should be the distance from the middle of the frame 
     * to the top of the frame, divided by the distance to the target.
     */
    final public static double Tv = 0.3839; // tangent of 21 degrees
    /**
     * Horizontal tangent of half the horizontal viewing angle.
     * At any distance this should be the distance from the middle of the frame 
     * to the side of the frame, divided by the distance to the target.
     */
    final public static double Th = 0.5095; // tangent of 27 degrees
    private AxisCamera camera = null;
    private ColorImage colorImage = null;
    private BinaryImage binaryImage = null;
    private ParticleAnalysisReport[] particleList = null;
    final public Preferences preferences;
    protected long pictureTime = 0L;
    private int threshHueMin, threshHueMax,
            threshSatMin, threshSatMax,
            threshBrightMin, threshBrightMax;
    private boolean useHSV = true;

    public Camera() {
        super();
        preferences = Preferences.getInstance();
        pictureTime = (System.currentTimeMillis() / (365 * 24 * 60 * 1000L)) * 100;
        (new Thread() {

            public void run() {
                try {
                    Thread.sleep(10 * 1000L);
                    initCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (RobotMap.DEBUG) {System.out.println("::: AxisCamera pre-initialized");}
            }
        }).start();

    }

    protected void initDefaultCommand() {
        // No default command for this subsystem.
    }

    /**
     * Make sure that the {@link AxisCamera} has been allocated and is
     * configured based on robot preferences.
     * Initialize the preferences if necessary.
     */
    protected synchronized void initCamera() {
        if (camera == null) {
            camera = AxisCamera.getInstance(RobotMap.CAMERA_IP_ADDR);
            camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedFlour2);
            camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        }
        if (!preferences.containsKey(RobotMap.CA_BRIGHTNESS_KEY)) {
            preferences.putInt(RobotMap.CA_BRIGHTNESS_KEY, camera.getBrightness());
        }

        
        if (!preferences.containsKey(RobotMap.CA_THRESH_HUE_MIN)) {
            preferences.putInt(RobotMap.CA_THRESH_HUE_MIN, 54);
            preferences.putInt(RobotMap.CA_THRESH_HUE_MAX, 139);
            preferences.putInt(RobotMap.CA_THRESH_SATURATION_MIN, 0);
            preferences.putInt(RobotMap.CA_THRESH_SATURATION_MAX, 255);
            preferences.putInt(RobotMap.CA_THRESH_BRIGHT_MIN, 20);
            preferences.putInt(RobotMap.CA_THRESH_BRIGHT_MAX, 255);
            preferences.putBoolean(RobotMap.CA_THRESH_USE_HSV, true);
            preferences.save();
        }

        camera.writeBrightness(preferences.getInt(RobotMap.CA_BRIGHTNESS_KEY, 75));
        threshHueMin = preferences.getInt(RobotMap.CA_THRESH_HUE_MIN, 54);
        threshHueMax = preferences.getInt(RobotMap.CA_THRESH_HUE_MAX, 139);
        threshSatMin = preferences.getInt(RobotMap.CA_THRESH_SATURATION_MIN, 0);
        threshSatMax = preferences.getInt(RobotMap.CA_THRESH_SATURATION_MAX, 255);
        threshBrightMin = preferences.getInt(RobotMap.CA_THRESH_BRIGHT_MIN, 20);
        threshBrightMax = preferences.getInt(RobotMap.CA_THRESH_BRIGHT_MAX, 255);
        useHSV = preferences.getBoolean(RobotMap.CA_THRESH_USE_HSV, true);
    }

    /**
     * Reset the camera and free internal images and data.
     */
    public void reset() {
        initCamera();
        try {
            if (colorImage != null) {
                colorImage.free();
                colorImage = null;
            }
            if (binaryImage != null) {
                binaryImage.free();
                binaryImage = null;
            }
        } catch (NIVisionException e) {
            e.printStackTrace();
        }
        particleList = null;
    }

    /**
     * Get an image from the camera.
     *
     * @return Whether image was successfully captured.
     */
    public boolean takePicture() {
        initCamera();
        if (!camera.freshImage()) {
            if (RobotMap.DEBUG) {System.out.println("::: Camera: no picture");}
            return false;
        }
        if (RobotMap.DEBUG) {System.out.println("::: Camera: takePicture: "
                + " brightness=" + camera.getBrightness());}
        long time = System.currentTimeMillis();
        try {
            colorImage = camera.getImage();
            pictureTime += 10;
            if (RobotMap.DEBUG) printParticleReport(particleList, 0.0, null, System.out);
        } catch (Exception e) {
            e.printStackTrace();
            particleList = null;
        }
        time = System.currentTimeMillis() - time;
        if (RobotMap.DEBUG) {System.out.println("::: Camera: takePicture() in " + time + " milliseconds");}
        return true;
    }
    
    /**
     * Load an image into the camera from a file on the cRIO.
     * This is useful for experimenting with image processing. It allows the 
     * same image file to be processed by different filters and parameters.
     * Returns false if the file cannot be found.
     * 
     * @param filename Full file path on the cRIO's file system.  E.g. "/tmp/pict_1.png".
     * @return Whether image was successfully captured.
     */
    public boolean loadPicture(String filename) {
        try {
            reset();
            colorImage = new RGBImage(filename);
            pictureTime += 10;
            return (colorImage != null) && (colorImage.getWidth() > 1);
        } catch (NIVisionException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Process the current image into a list of particles.
     *
     * @param dist estimated distance to target in inches.
     * @param saveFiles whether to save interim files for debugging purposes.
     * @return whether processing completed successfully.
     */
    public boolean processImage(double dist, boolean saveFiles) {
        if (colorImage == null) {
            System.err.println("You must take a picture first");
            return false;
        }
        try {
            return processImage(this.colorImage, dist, saveFiles);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Process an image into a list of particles.
     *
     * @param image Original image.
     * @param dist estimated distance to target in inches.
     * @param saveFiles whether to save interim files for debugging purposes.
     * @return whether processing completed successfully.
     */
    public boolean processImage(ColorImage image, double dist, boolean saveFiles)
            throws NIVisionException, IOException {

        long time = System.currentTimeMillis();
        BinaryImage tempImage = null;
        if (saveFiles) {
            image.write("/tmp/pict_" + pictureTime + "_0.png");
        }

        // Convert color image to binary image using thresholds
        if (binaryImage != null) {
            binaryImage.free();
            binaryImage = null;
        }
        if (useHSV) {
            binaryImage = image.thresholdHSV(threshHueMin, threshHueMax,
                    threshSatMin, threshSatMax,
                    threshBrightMin, threshBrightMax);
        } else {
            binaryImage = image.thresholdRGB(threshHueMin, threshHueMax,
                    threshSatMin, threshSatMax,
                    threshBrightMin, threshBrightMax);
        }

        if (saveFiles) {
            binaryImage.write("/tmp/pict_" + pictureTime + "_1.png");
        }

        // Remove small objects from binary image
        tempImage = binaryImage.removeSmallObjects(false, 2);
        binaryImage.free();
        binaryImage = tempImage;
        tempImage = null;
        if (saveFiles) {
            binaryImage.write("/tmp/pict_" + pictureTime + "_2.png");
        }

        // Apply convex hull transform to binary image to fill in holes
        tempImage = binaryImage.convexHull(false);
        binaryImage.free();
        binaryImage = tempImage;
        tempImage = null;
        if (saveFiles) {
            binaryImage.write("/tmp/pict_" + pictureTime + "_3.png");
        }

        // Remove particles from binary image that can't be the right size.
        double expectedWidth = image.getWidth() * 24.0 / (dist * Th * 2);
        double expectedHeight = image.getHeight() * 18.0 / (dist * Tv * 2);
        double expectedArea = expectedHeight * expectedHeight * 1.3;
        CriteriaCollection cc = new CriteriaCollection();
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH,
                (int) (expectedWidth * (1.0 - RECT_MARGIN)),
                (int) (expectedWidth * (1.0 + RECT_MARGIN)), false);
        cc.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT,
                (int) (expectedHeight * (1.0 - RECT_MARGIN)),
                (int) (expectedHeight * (1.0 + RECT_MARGIN)), false);
        cc.addCriteria(MeasurementType.IMAQ_MT_CONVEX_HULL_AREA,
                (int) (expectedArea * (1.0 - AREA_MARGIN)),
                (int) (expectedArea * (1.0 + AREA_MARGIN)), false);
        tempImage = binaryImage.particleFilter(cc);
        binaryImage.free();
        binaryImage = tempImage;
        tempImage = null;
        if (saveFiles) {
            binaryImage.write("/tmp/pict_" + pictureTime + "_4.png");
        }

        // Retrieve an array of particles from the binary image
        particleList = binaryImage.getOrderedParticleAnalysisReports();

        // Sort the array of particles so the best ones are first
        ParticleComparer particleComparer = new ParticleComparer(dist);
        Arrays.sort(particleList, particleComparer);

        if (saveFiles) {
            FileConnection fc = (FileConnection) Connector.open("file:///"
                    + "/tmp/pict_" + pictureTime + "_0.html", Connector.WRITE);
            fc.create();
            PrintStream out = new PrintStream(fc.openOutputStream());
            printParticleReport(particleList, dist, cc, out);
            fc.close();
            if (RobotMap.DEBUG) {System.out.println("::: Camera: files saved");}
        }

        binaryImage.free();
        binaryImage = null;
        time = System.currentTimeMillis() - time;
        if (RobotMap.DEBUG) {System.out.println("::: Camera: processImage(" + saveFiles + ") in " + time + " milliseconds");}
        return true;
    }

    /**
     * Get the array of particles from the most recent image.
     *
     * @return Array of particles or null.
     */
    public ParticleAnalysisReport[] getParticles() {
        return particleList;
    }

    /**
     * Return the best particle representing a backboard.
     * Returns null if no backboard is seen.
     * 
     * @return particle or null.
     */
    public ParticleAnalysisReport getBackboard() {
        if (particleList == null) {
            return null;
        }
        if (particleList.length == 0) {
            return null;
        }
        return particleList[0];
    }

    /**
     * Print out the array of particles in HTML.
     */
    protected void printParticleReport(ParticleAnalysisReport[] particles,
            double dist, CriteriaCollection cc, PrintStream out) {
        out.println("<html><body>");
        if (camera != null) {

            out.println("<br/><br/><ul>");
            out.println("\t\t<li>brightness = " + camera.getBrightness() + "</li>");
            out.println("\t\t<li>colorLevel = " + camera.getColorLevel() + "</li>");
            out.println("\t\t<li>whiteBalance = " + camera.getWhiteBalance().value + "</li>");
            out.println("\t\t<li>exposureControl = " + camera.getExposureControl().value + "</li>");
            if (dist > 0.0) {
                out.println("\t\t<li>distance = " + dist + " inches</li>");
            }
            double imageWidth = (particles!=null && particles.length>0) ? particles[0].imageWidth : 640;
            double imageHeight = (particles!=null && particles.length>0) ? particles[0].imageHeight : 480;
            double expectedWidth = imageWidth * 24.0 / (dist * Th * 2);
            double expectedHeight = imageHeight * 18.0 / (dist * Tv * 2);
            out.println("\t\t<li> width: " + Math.floor(expectedWidth * (1.0 - RECT_MARGIN)) 
                    + " to " + Math.floor(expectedWidth * (1.0 + RECT_MARGIN)) + "</li>");
            out.println("\t\t<li> height: " + Math.floor(expectedHeight * (1.0 - RECT_MARGIN)) 
                    + " to " + Math.floor(expectedHeight * (1.0 + RECT_MARGIN)) + "</li>");
            out.println("\t\t<li>Thresholding: " + (useHSV ? "HSV" : "RGB"));
            out.println("\t\t<ul>");
            out.println("\t\t\t<li>" + (useHSV ? "Hue " : "Red ") 
                    + threshHueMin + " to " + threshHueMax + "</li>");
            out.println("\t\t\t<li>" + (useHSV ? "Sat " : "Green ") 
                    + threshSatMin + " to " + threshSatMax + "</li>");
            out.println("\t\t\t<li>" + (useHSV ? "Val " : "Blue ") 
                    + threshBrightMin + " to " + threshBrightMax + "</li>");
            out.println("\t\t</ul>");
            out.println("\t\t</li>");
            out.println("</ul>");
        }
        if (particles == null || particles.length == 0) {
            out.println("<p>No particles returned</p>");
            return;
        }
        
        ParticleComparer particleComparer = new ParticleComparer(dist);
        out.println("\n\n<h1>" + pictureTime + "</h1>");
        out.println("<table border=\"0\" rules=\"all\" cellpadding=\"2\" cellspacing=\"1\"><thead>");
        out.println("<tr><th>n</th><th>location</th><th>size</th><th>area</th>"
                + "<th>distance</th><th>angle</th><th>ratio</th>"
                + "<th>score</th></tr>");
        out.println("</thead><tbody>");
        for (int i = 0; i < particles.length; i++) {
            ParticleAnalysisReport p = particles[i];
            int distance = (int) estimateDistance(p);
            int angle = (int) estimateAngle(p, distance);
            int score = particleComparer.score(p);
            double ratio = (1.0 * p.boundingRectWidth / p.boundingRectHeight);
            out.println("<tr>\t<td>" + i + "</td>");
            out.println("\t<td>" + p.boundingRectLeft + ", " + p.boundingRectTop + "</td>");
            out.println("\t<td>" + p.boundingRectWidth + ", " + p.boundingRectHeight + "</td>" );
            out.println("\t<td>" + p.particleArea + "</td>");
            out.println("\t<td>" + distance + "</td>");
            out.println("\t<td>" + angle + "</td>");
            out.println("\t<td>" + ratio + "</td>");
            out.println("\t<td>" + score + "</td>");
            out.println("</tr>");
        }
        out.println("</tbody></table>");
        
        String fname = "pict_" + pictureTime + "_0.png";

        out.println("<br/><br/><p><img src=\"" + fname + "\"/></p>");
        fname = "pict_" + pictureTime + "_4.png";
        out.println("<p><img src=\"" + fname + "\"/></p>");
        out.println("</body></html>");
    }
    
    public void updateStatus() {
        if (RobotMap.DEBUG) 
            SmartDashboard.putNumber("Camera particles", 
                    particleList==null ? 0 : particleList.length);
    }

    /**
     * @return estimated distance in inches.
     */
    static public double estimateDistance(ParticleAnalysisReport particle) {
        double y = 18.0 / 2; // half the height of backboard, in inches
        double y1 = particle.boundingRectHeight / 2.0; // pixels
        double z = (particle.imageHeight * y) / (y1 * 2 * Tv);
        return z * DISTANCE_FUDGE;
    }

    /**
     * @param dist distance to target in inches.
     * @return estimated x deflection, measured in inches.
     */
    static public double estimateDeflection(ParticleAnalysisReport particle, double dist) {
        double c = particle.imageWidth / 2.0; // center in pixels
        double x1 = c - particle.center_mass_x; // pixels
        double x = (-2 * Th * x1 * dist) / particle.imageWidth;
        return x;
    }

    /**
     * @param dist distance to target in inches.
     * @return estimated angle to target, in degrees.
     */
    static public double estimateAngle(ParticleAnalysisReport particle, double dist) {
        double x = estimateDeflection(particle, dist);
        double th = MathUtils.atan2(x, dist);
        return Math.toDegrees(th);
    }

    /**
     * Compare particles based on their location within the image. Particles in
     * the horizontal middle and high up score highest. Comparer objects are
     * used to sort arrays of particles so the best particles are listed first.
     */
    static class ParticleComparer implements Comparer {

        /**
         * Estimated distance to target in inches.
         */
        final double dist;

        public ParticleComparer(double d) {
            this.dist = d;
        }

        /**
         * Give a score indicating how close the particle's ratio is to the
         * backboard.
         *
         * @return an integer from 0 to 2.
         */
        protected int ratioScore(ParticleAnalysisReport p) {
            double ratio = (1.0 * p.boundingRectWidth / p.boundingRectHeight);
            double diff = Math.abs(ratio - 1.33);
            return (diff < 0.2) ? 2 : (diff < 0.4 ? 1 : 0);
        }

        /**
         * Give a score indicating how close the particle is to the horizontal
         * center of the image.
         *
         * @return an integer from 0 to 10.
         */
        protected int horizontalScore(ParticleAnalysisReport p) {
            double s = Math.abs(p.center_mass_x_normalized * 5);
            return 2 * (int) Math.floor(5.0 - s);
        }

        /**
         * Give a score indicating how high up in the image the particle is.
         *
         * @return an integer from 0 to 10.
         */
        protected int verticalScore(ParticleAnalysisReport p) {
            double s = 1.0 - p.center_mass_y_normalized;
            return (int) Math.floor(s * 5);
        }

        protected int score(ParticleAnalysisReport p) {
            return ratioScore(p) * 100 + horizontalScore(p) * 10 + verticalScore(p);
        }

        /**
         * Compare two particles based on how close to the horizontal center
         * they are. If both particles are in the middle, compare based on how
         * high up they are.
         */
        public int compare(Object o1, Object o2) {
            ParticleAnalysisReport p1 = (ParticleAnalysisReport) o1;
            ParticleAnalysisReport p2 = (ParticleAnalysisReport) o2;
            return score(p2) - score(p1);
        }
    };
}
