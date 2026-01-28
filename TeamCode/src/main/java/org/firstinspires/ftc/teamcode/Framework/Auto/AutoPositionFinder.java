package org.firstinspires.ftc.teamcode.Framework.Auto;

import static org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive.PARAMS;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Config
@Autonomous(name = "Auto Position Finder", group = "Tools")
public class AutoPositionFinder extends BaseAuto {
    public static double posX = 0;
    public static double posY = 0;
    public static double heading = 0;

    //if apriltags are detected, then should we just use those, or also use odo pods with apriltags
    public static boolean ODO_PODS_AVG_WITH_CAM = true;
    private double xSum;
    private double ySum;
    private double headingSum;
    private AprilTagDetection[] tagDetections;
    Pose2d startPos;
    Pose2d currentPos;
    Pose2d currentCamPos;
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(true);
        ThreeDeadWheelLocalizer localizer = new ThreeDeadWheelLocalizer(hardwareMap, PARAMS.inPerTick, new Pose2d(posX,posY,heading));
        ThreeDeadWheelLocalizer camLocalizer = new ThreeDeadWheelLocalizer(hardwareMap, PARAMS.inPerTick, new Pose2d(posX,posY,heading));
        currentPos = new Pose2d(posX,posY,Math.toRadians(heading));
        currentCamPos = new Pose2d(posX,posY,Math.toRadians(heading));
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.triangleWasPressed()) {
                localizer.setPose(new Pose2d(posX, posY, Math.toRadians(heading)));
                camLocalizer.setPose(new Pose2d(posX, posY, Math.toRadians(heading)));
            }
            localizer.update();
            //camLocalizer.update();

            //variables to make an avg position, assuming multiple tag detections differ
            tagDetections = vision.getTags();
            xSum = 0;
            ySum = 0;
            headingSum = 0;

            /*for(AprilTagDetection tag : tagDetections){
                if(tag != null && !(tag.metadata.fieldPosition.get(0) == 0 && tag.metadata.fieldPosition.get(1) == 0 && tag.metadata.fieldPosition.get(2) == 0))
                {
                    xSum += tag.robotPose.getPosition().x;
                    ySum += tag.robotPose.getPosition().y;

                    headingSum += tag.robotPose.getOrientation().getYaw();
                }
            }*/


            currentPos = localizer.getPose();
            currentCamPos = camLocalizer.getPose();

            /*if(ODO_PODS_AVG_WITH_CAM || tagDetections.length == 0){
                xSum += currentCamPos.position.x;
                ySum += currentCamPos.position.y;
                headingSum += Math.toDegrees(currentCamPos.heading.toDouble());

                camLocalizer.setPose(
                        new Pose2d(
                                xSum/(tagDetections.length+1),
                                ySum/(tagDetections.length+1),
                                Math.toRadians(headingSum/(tagDetections.length+1))
                        )
                );
            }else{
                camLocalizer.setPose(
                        new Pose2d(
                                xSum/(tagDetections.length),
                                ySum/(tagDetections.length),
                                Math.toRadians(headingSum/(tagDetections.length))
                        )
                );
            }*/

            currentCamPos = updateLocalizer(true,camLocalizer);
            telemetry.addData("X Pos",currentPos.position.x);
            telemetry.addData("Y Pos",currentPos.position.y);
            telemetry.addData("Heading (deg)",Math.toDegrees(currentPos.heading.toDouble()));

            telemetry.addData("Camera Corrected X Pos",currentCamPos.position.x);
            telemetry.addData("Camera Corrected Y Pos",currentCamPos.position.y);
            telemetry.addData("Camera Corrected Heading (deg)",Math.toDegrees(currentCamPos.heading.toDouble()));
            telemetry.update();
        }
    }


}