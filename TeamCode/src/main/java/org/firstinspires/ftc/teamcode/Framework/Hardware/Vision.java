package org.firstinspires.ftc.teamcode.Framework.Hardware;

import android.graphics.Color;
import android.util.Size;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.hardware.camera.*;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;


import java.util.List;
import java.util.concurrent.TimeUnit;

@Config
public class Vision {
    //"Constants" that can be changed by FTC Dashboard
    public static double FOCAL_LENGTH = 617;
    public static double DENSITY_MIN = 0.2;
    public static double DENSITY_MAX = 1;
    public static double CIRCULARITY_MIN = 0.4;
    public static double CIRCULARITY_MAX = 1;
    public static double CONTOUR_AREA_MIN = 400;
    public static double CONTOUR_AREA_MAX = 200_000;

    /* Logitech C920 ranges:
    Exposure 0 to 204 ms
    Gain 0 to 255
     */
    public static int GAIN = 150;
    public static long EXPOSURE = 150;


    private CameraName name;
    private AprilTagProcessor aprilTag;
    private VisionPortal visionPortal;
    private ColorBlobLocatorProcessor.Builder artifactDetectorBuilder;
    private ColorBlobLocatorProcessor greenArtifactDetector;
    private ColorBlobLocatorProcessor purpleArtifactDetector;

    //change these units depending on what we need -- currently inches and degrees
    private DistanceUnit distanceUnit = DistanceUnit.INCH;
    private AngleUnit angleUnit = AngleUnit.DEGREES;
    private int camWidth=640;
    private int camHeight=480;
    
    private GainControl gainControl;
    private ExposureControl exposureControl;


    /**
     * Constructs the Vision class, including a VisionPortal and two processors for both Blob (Artifact) and
     * AprilTag detection.
     * @param cameraName the CameraName of a camera obtained through the HardwareMap
     */
    public Vision(CameraName cameraName){
        this.name = cameraName;
        this.aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary())
                .setOutputUnits(distanceUnit, angleUnit)
                .build();
        this.artifactDetectorBuilder = new ColorBlobLocatorProcessor.Builder()
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                //change top based on where camera is... ROI needs (probably) to exclude the obelisk
                .setRoi(ImageRegion.asUnityCenterCoordinates(-1, 0.4, 1, -1))
                .setDrawContours(true)   // Show contours on the Stream Preview -- can disable for performance
                .setBoxFitColor(0)       // Disable the drawing of rectangles
                .setBlurSize(5)          // Smooth the transitions between different colors in image

                // the following options have been added to fill in perimeter holes.
                .setDilateSize(35)       // Expand blobs to fill any divots on the edges
                .setErodeSize(35)        // Shrink blobs back to original size
                .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING);
        //Make two processors for the different artifact colors
        this.purpleArtifactDetector = artifactDetectorBuilder
                .setTargetColorRange(ColorRange.ARTIFACT_PURPLE)
                .setCircleFitColor(Color.rgb(0, 255, 255)) //draw circlefit with a color of cyan
                .build();
        this.greenArtifactDetector = artifactDetectorBuilder
                .setTargetColorRange(ColorRange.ARTIFACT_GREEN)
                .setCircleFitColor(Color.rgb(255, 255, 0)) //draw circlefit with a color of yellow
                .build();
        this.visionPortal = new VisionPortal.Builder()
                .setCamera(cameraName)
                .addProcessor(this.aprilTag)
                .addProcessor(this.purpleArtifactDetector)
                .addProcessor(this.greenArtifactDetector)
                //lower resolution possibly might make code faster if possible (ex 320, 240)
                //however, camera will need to be calibrated and itll probably be a pain
                .setCameraResolution(new Size(camWidth, camHeight))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();
        while(visionPortal.getCameraState()!= VisionPortal.CameraState.STREAMING){}
        this.gainControl = visionPortal.getCameraControl(GainControl.class);
        this.exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        exposureControl.setMode(ExposureControl.Mode.Manual);
        gainControl.setGain(GAIN);
        exposureControl.setExposure(EXPOSURE,TimeUnit.MILLISECONDS);




    }

    private void exposureUpdate(){
        //TODO: DELETE THIS AFTER FINDING OPTIMAL EXPOSURE
        this.gainControl = visionPortal.getCameraControl(GainControl.class);
        this.exposureControl = visionPortal.getCameraControl(ExposureControl.class);

        if(exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
        }
        gainControl.setGain(GAIN);
        exposureControl.setExposure(EXPOSURE,TimeUnit.MILLISECONDS);
    }
    /**
     * Get AprilTags detected by the camera
     * @return Object[] of AprilTags (can be cast to AprilTagDetection)
     */
    public Object[] getTags(){
        exposureUpdate();
        List<AprilTagDetection> detectionsList;
        detectionsList = this.aprilTag.getDetections();
        return detectionsList.toArray(new AprilTagDetection[0]);
    }

    /**
     * Gets essential pose info about an AprilTag
     * @param tag AprilTagDetection
     * @return double[] with the distance to the tag the bearing angle to the tag,
     *          the elevation angle to the tag, and the yaw in that order
     */
    public double[] getTagNav(AprilTagDetection tag){
        double poseX = tag.ftcPose.x;
        double poseY = tag.ftcPose.y;
        double poseZ = tag.ftcPose.z;
        double posePitch = tag.ftcPose.pitch;
        double poseRoll = tag.ftcPose.roll;
        double poseYaw = tag.ftcPose.yaw;

        double poseRange = tag.ftcPose.range;
        double poseBearing = tag.ftcPose.bearing;
        double poseElevation = tag.ftcPose.elevation;
        return new double[] {poseRange, poseBearing, poseElevation, poseYaw};
    }

    /**
     * Get Green-colored Blobs (likely Green Artifacts) detected by the camera
     * @return Object[] of Blobs (can be cast to ColorBlobLocatorProcessor.Blob)
     */
    public Object[] getGreenArtifacts(){
        exposureUpdate();
        List<ColorBlobLocatorProcessor.Blob> blobs;
        blobs = this.greenArtifactDetector.getBlobs();

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                CONTOUR_AREA_MIN+150, CONTOUR_AREA_MAX, blobs);

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CIRCULARITY,
                CIRCULARITY_MIN, CIRCULARITY_MAX, blobs);

        ColorBlobLocatorProcessor.Util.filterByCriteria(ColorBlobLocatorProcessor.BlobCriteria.BY_DENSITY,DENSITY_MIN, DENSITY_MAX, blobs);
        return blobs.toArray(new ColorBlobLocatorProcessor.Blob[0]);
    }

    /**
     * Get Purple-colored Blobs (likely Purple Artifacts) detected by the camera
     * @return Object[] of Blobs (can be cast to ColorBlobLocatorProcessor.Blob)
     */
    public Object[] getPurpleArtifacts(){
        exposureUpdate();
        List<ColorBlobLocatorProcessor.Blob> blobs;
        blobs = this.purpleArtifactDetector.getBlobs();

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CONTOUR_AREA,
                CONTOUR_AREA_MIN, CONTOUR_AREA_MAX, blobs);

        ColorBlobLocatorProcessor.Util.filterByCriteria(
                ColorBlobLocatorProcessor.BlobCriteria.BY_CIRCULARITY,
                CIRCULARITY_MIN, CIRCULARITY_MAX, blobs);

        ColorBlobLocatorProcessor.Util.filterByCriteria(ColorBlobLocatorProcessor.BlobCriteria.BY_DENSITY,DENSITY_MIN, DENSITY_MAX, blobs);
        return blobs.toArray(new ColorBlobLocatorProcessor.Blob[0]);
    }

    /**
     * Analyzes a Blob to get the (approximate) location of an artifact and angle to the artifact
     * @param blob The Blob to analyze
     * @return double[] with the distance in inches and the angle
     */
    public double[] getArtifactLocation(ColorBlobLocatorProcessor.Blob blob){
        //Triangle Similarity: https://pyimagesearch.com/2015/01/19/find-distance-camera-objectmarker-using-python-opencv/
        Circle circle = blob.getCircle();
        double diameter = circle.getRadius()*2;
        double realWorldDist = (5.0*FOCAL_LENGTH)/diameter;

        //https://stackoverflow.com/questions/1211212/how-to-calculate-an-angle-from-three-points

        //I originally wrote this code to assume that (0,0) was the center then found out it is not :(
        // so I changed the variables to do that
        double horizontalDist = circle.getCenter().x-(double)this.camWidth/2;
        double verticalDist = (double)this.camHeight/2 - circle.getCenter().y;
        double diagonalDist = Math.sqrt(Math.pow(0-horizontalDist,2)+Math.pow((double)this.camHeight/-2 - verticalDist,2));

        double angle = Math.acos(
                (
                        Math.pow((double)camHeight/-2 - verticalDist,2)+
                        Math.pow(diagonalDist,2)-
                        Math.pow(horizontalDist,2)
                )/(2* Math.abs((double)camHeight/-2 - verticalDist) * diagonalDist));
        //converting to degrees if needed
        angle = this.angleUnit == AngleUnit.DEGREES ? Math.toDegrees(angle) : angle;

        //converting to negative if needed
        angle = horizontalDist > 0 ? angle : -angle;

        return new double[] {realWorldDist, angle};
    }

}