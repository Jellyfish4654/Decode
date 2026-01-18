package org.firstinspires.ftc.teamcode.Framework.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.Params;
import org.firstinspires.ftc.teamcode.JellyTele;

@Config
@Autonomous(name = "Auto Position Finder")
public class AutoPositionFinder extends BaseAuto {
    public static double posX = 0;
    public static double posY = 0;
    public static double heading = 0;

    long startVelTime = 0;
    Pose2d startPos;
    Pose2d currentPos;
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(true);
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(posX,posY,heading));
        currentPos = new Pose2d(posX,posY,heading);
        waitForStart();
        while(opModeIsActive()){
            if(gamepad1.triangleWasPressed()){
                drive.localizer.setPose(new Pose2d(posX,posY,heading));
            }
            drive.localizer.update();
            currentPos = drive.localizer.getPose();
            telemetry.addData("X Pos",currentPos.position.x);
            telemetry.addData("Y Pos",currentPos.position.y);
            telemetry.addData("Heading (deg)",Math.toDegrees(currentPos.heading.toDouble()));
            telemetry.update();
        }
    }


}