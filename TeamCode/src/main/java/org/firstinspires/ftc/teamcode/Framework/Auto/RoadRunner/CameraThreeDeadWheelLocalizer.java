package org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.PoseVelocity2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.Framework.Hardware.Vision;
import org.firstinspires.ftc.teamcode.Framework.Params.Alliance;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

public class CameraThreeDeadWheelLocalizer extends ThreeDeadWheelLocalizer{
    public static Vector2d RED_GOAL_POS = new Vector2d(-65,65);
    public static Vector2d BLUE_GOAL_POS = new Vector2d(-65,-65);
    
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
        super.update();

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
                    Math.toRadians((headingSum) / validTagAmount)
            );
            weightVisionFully = false;

        }else {
            currentPos = new Pose2d(
                    (currentPos.position.x + xSum) / (validTagAmount + 1),
                    (currentPos.position.y + ySum) / (validTagAmount + 1),
                    Math.toRadians((Math.toDegrees(currentPos.heading.toDouble()) + headingSum) / (validTagAmount + 1))
            );
        }
        super.setPose(currentPos);

        return super.update();
    }
    
    private double[] getGoalRelativeLocation(Alliance alliance) {
        Pose2d currentPose = getPose();
        double xDist = currentPose.position.x - (alliance == Alliance.RED ? RED_GOAL_POS.x : BLUE_GOAL_POS.x);
        double yDist = currentPose.position.y - (alliance == Alliance.RED ? RED_GOAL_POS.y : BLUE_GOAL_POS.y);
        double bearing = Math.toDegrees(currentPose.heading.toDouble()) - 180 - (org.firstinspires.ftc.teamcode.Framework.Params.alliance == Alliance.RED ? -1 : 1) * Math.toDegrees(Math.atan(yDist / xDist));
        return new double[] {xDist, yDist, bearing};
    }
    
    public double getGoalBearing(Alliance alliance) {
        return getGoalRelativeLocation(alliance)[2];
    }
    
    public double getGoalDistance(Alliance alliance) {
        return Math.sqrt(
                Math.pow(getGoalRelativeLocation(alliance)[0],2)+
                Math.pow(getGoalRelativeLocation(alliance)[1],2)
        );
    }
}