package org.firstinspires.ftc.teamcode.Framework.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
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
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(posX,posY,heading));
        MecanumDrive camDrive = new MecanumDrive(hardwareMap, new Pose2d(posX,posY,heading));
        currentPos = new Pose2d(posX,posY,heading);
        currentCamPos = new Pose2d(posX,posY,heading);
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.triangleWasPressed()){
                drive.localizer.setPose(new Pose2d(posX,posY,heading));
                camDrive.localizer.setPose(new Pose2d(posX,posY,heading));
            }
            drive.localizer.update();
            camDrive.localizer.update();

            //variables to make an avg position, assuming multiple tag detections differ
            tagDetections = vision.getTags();
            xSum = 0;
            ySum = 0;
            headingSum = 0;

            for(AprilTagDetection tag : tagDetections){
                xSum += tag.robotPose.getPosition().x;
                ySum += tag.robotPose.getPosition().y;

                headingSum += tag.robotPose.getOrientation().getYaw();
            }


            currentPos = drive.localizer.getPose();
            currentCamPos = camDrive.localizer.getPose();

            if(ODO_PODS_AVG_WITH_CAM || tagDetections.length == 0){
                xSum += currentCamPos.position.x;
                ySum += currentCamPos.position.y;
                headingSum += currentCamPos.heading.toDouble();

                camDrive.localizer.setPose(
                        new Pose2d(
                                xSum/(tagDetections.length+1),
                                ySum/(tagDetections.length+1),
                                headingSum/(tagDetections.length+1)
                        )
                );
            }else{
                camDrive.localizer.setPose(
                        new Pose2d(
                                xSum/(tagDetections.length),
                                ySum/(tagDetections.length),
                                headingSum/(tagDetections.length)
                        )
                );
            }
            currentCamPos = camDrive.localizer.getPose();
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