package org.firstinspires.ftc.teamcode.Framework.Auto;

import static org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive.PARAMS;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.CameraThreeDeadWheelLocalizer;
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
        ThreeDeadWheelLocalizer camLocalizer = new CameraThreeDeadWheelLocalizer(hardwareMap, PARAMS.inPerTick, new Pose2d(posX,posY,heading),vision);
        currentPos = new Pose2d(posX,posY,Math.toRadians(heading));
        currentCamPos = new Pose2d(posX,posY,Math.toRadians(heading));
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.triangleWasPressed()) {
                localizer.setPose(new Pose2d(posX, posY, Math.toRadians(heading)));
                camLocalizer.setPose(new Pose2d(posX, posY, Math.toRadians(heading)));
            }
            localizer.update();
            camLocalizer.update();

            //variables to make an avg position, assuming multiple tag detections differ
            xSum = 0;
            ySum = 0;
            headingSum = 0;

            currentPos = localizer.getPose();
            currentCamPos = camLocalizer.getPose();

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