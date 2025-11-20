package org.firstinspires.ftc.teamcode.Framework.Hardware;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.*;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagGameDatabase;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.ArrayList;


public class Vision {
    CameraName name;
    AprilTagProcessor aprilTag;
    VisionPortal visionPortal;


    public Vision(CameraName camname){
        this.name = camname;
        this.aprilTag = new AprilTagProcessor.Builder()
                .setDrawAxes(true)
                .setTagLibrary(AprilTagGameDatabase.getCurrentGameTagLibrary())
                .build();
        this.visionPortal = new VisionPortal.Builder()
                .setCamera(camname)
                .addProcessor(this.aprilTag)
                .setCameraResolution(new Size(640, 480))
                .setStreamFormat(VisionPortal.StreamFormat.YUY2)
                .enableLiveView(true)
                .setAutoStopLiveView(true)
                .build();

    }

    public Object[] getTags(){
        ArrayList<AprilTagDetection> detectionsList;
        detectionsList = this.aprilTag.getDetections();
        return detectionsList.toArray(new AprilTagDetection[0]);
    }

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
        return new double[] {poseRange, poseBearing, poseElevation};
    }

}
