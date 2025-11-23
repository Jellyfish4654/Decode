package org.firstinspires.ftc.teamcode.Framework.Hardware;

import android.graphics.Color;
import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.*;
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


public class Vision {
    CameraName name;
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;
    ColorBlobLocatorProcessor.Builder artifactDetectorBuilder;
    ColorBlobLocatorProcessor greenArtifactDetector;
    ColorBlobLocatorProcessor purpleArtifactDetector;

    //change these units depending on what we need -- currently inches and degrees
    DistanceUnit distanceUnit = DistanceUnit.INCH;
    AngleUnit angleUnit = AngleUnit.DEGREES;
    int camWidth=640;
    int camHeight=480;
    //TODO: Calibrate this? (it must be in inches)
    double focalLength = 0.03937008;

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
                .setRoi(ImageRegion.asUnityCenterCoordinates(-0.75, 0.4, 0.75, -1))
                .setDrawContours(true)   // Show contours on the Stream Preview -- can disable for performance
                .setBoxFitColor(0)       // Disable the drawing of rectangles
                .setBlurSize(5)          // Smooth the transitions between different colors in image

                // the following options have been added to fill in perimeter holes.
                .setDilateSize(15)       // Expand blobs to fill any divots on the edges
                .setErodeSize(15)        // Shrink blobs back to original size
                .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING);
        //Make two processors for the different artifact colors
        this.purpleArtifactDetector = artifactDetectorBuilder
                .setTargetColorRange(ColorRange.ARTIFACT_PURPLE)
                .setCircleFitColor(Color.rgb(0, 255, 255)) //draw circlefit with a color of cyan
                .build();
        this.greenArtifactDetector = artifactDetectorBuilder
                .setTargetColorRange(ColorRange.ARTIFACT_PURPLE)
                .setCircleFitColor(Color.rgb(255, 255, 0)) //draw circlefit with a color of yellow
                .build();
        this.visionPortal = new VisionPortal.Builder()
                .setCamera(cameraName)
                .addProcessor(this.aprilTag)
                .addProcessor(this.purpleArtifactDetector)
                .addProcessor(this.greenArtifactDetector)
                //lower resolution possibly might make code faster if possible (ex 320, 240)
                //however, camera will need to be calibrated
                .setCameraResolution(new Size(camWidth, camHeight))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();


    }

    /**
     * Get AprilTags detected by the camera
     * @return Object[] of AprilTags (can be cast to AprilTagDetection)
     */
    public Object[] getTags(){

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
        List<ColorBlobLocatorProcessor.Blob> blobs;
        blobs = this.greenArtifactDetector.getBlobs();
        return blobs.toArray(new ColorBlobLocatorProcessor.Blob[0]);
    }

    /**
     * Get Purple-colored Blobs (likely Purple Artifacts) detected by the camera
     * @return Object[] of Blobs (can be cast to ColorBlobLocatorProcessor.Blob)
     */
    public Object[] getPurpleArtifacts(){
        List<ColorBlobLocatorProcessor.Blob> blobs;
        blobs = this.purpleArtifactDetector.getBlobs();
        return blobs.toArray(new ColorBlobLocatorProcessor.Blob[0]);
    }

    /**
     * Analyzes a Blob to get the (approximate) location of an artifact and angle to the artifact
     * @param blob The Blob to analyze
     * @return double[] with the distance in inches and the angle in this.
     */
    public double[] getArtifactLocation(ColorBlobLocatorProcessor.Blob blob){
        //Triangle Similarity: https://pyimagesearch.com/2015/01/19/find-distance-camera-objectmarker-using-python-opencv/
        Circle circle = blob.getCircle();
        double diameter = circle.getRadius()*2;
        double realWorldDist = (5.0*this.focalLength)/diameter;

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
