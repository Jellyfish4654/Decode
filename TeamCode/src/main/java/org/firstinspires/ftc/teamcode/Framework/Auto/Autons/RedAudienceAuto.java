package org.firstinspires.ftc.teamcode.Framework.Auto.Autons;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Framework.Auto.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;

@Config
@Autonomous(name = "Red Audience", preselectTeleOp = "JellyTele")
public class RedAudienceAuto extends BaseOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        Pose2d initialPose = new Pose2d(61.5, 23.5, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder moveToScan;
        Pose2d scanPose;
        TrajectoryActionBuilder moveToShoot;
        Pose2d shootPose;
        SequentialAction shoot;
        TrajectoryActionBuilder moveToPark;

        waitForStart();
        if (!isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction()
        );

    }
}