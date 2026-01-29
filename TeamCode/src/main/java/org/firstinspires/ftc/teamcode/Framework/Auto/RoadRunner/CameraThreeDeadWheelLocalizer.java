package org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Vision;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class CameraThreeDeadWheelLocalizer extends ThreeDeadWheelLocalizer{
    Vision vision;
    boolean weightVisionFully;
    public CameraThreeDeadWheelLocalizer(HardwareMap hardwareMap, double inPerTick, Pose2d initialPose, Vision vision) {
        super(hardwareMap, inPerTick, initialPose);
        this.vision = vision;

        if(initialPose.position.x==0&&initialPose.position.y==0){
            weightVisionFully = true;
        }
    }

    @Override
    public PoseVelocity2d update(){
        PoseVelocity2d ret = super.update();

        double xSum;
        double ySum;
        double headingSum;
        int validTagAmount = 0;
        Pose2d currentPos;
        AprilTagDetection[] tagDetections;

        currentPos = super.getPose();
        tagDetections = vision.getTags();
        xSum = 0;
        ySum = 0;
        headingSum = 0;
        for(AprilTagDetection tag : tagDetections){
            if(tag != null) {
                if (!(tag.metadata.fieldPosition.get(0) == 0 && tag.metadata.fieldPosition.get(1) == 0 && tag.metadata.fieldPosition.get(2) == 0)) {
                    xSum += tag.robotPose.getPosition().x;
                    ySum += tag.robotPose.getPosition().y;
                    
                    headingSum += tag.robotPose.getOrientation().getYaw(AngleUnit.DEGREES);
                    validTagAmount++;
                }
            }
        }
        if(weightVisionFully && validTagAmount >= 1){
            currentPos = new Pose2d(
                    xSum / validTagAmount,
                    ySum / validTagAmount,
                    currentPos.heading.toDouble()
                    //Math.toRadians((headingSum) / validTagAmount)
            );
            if(validTagAmount>0){
                weightVisionFully = false;
            }
        }else {
            currentPos = new Pose2d(
                    (currentPos.position.x + xSum) / (validTagAmount + 1),
                    (currentPos.position.y + ySum) / (validTagAmount + 1),
                    currentPos.heading.toDouble()
                    //Math.toRadians((Math.toDegrees(currentPos.heading.toDouble()) + headingSum) / (validTagAmount + 1))
            );
        }
        super.setPose(currentPos);

        return ret;
    }


}