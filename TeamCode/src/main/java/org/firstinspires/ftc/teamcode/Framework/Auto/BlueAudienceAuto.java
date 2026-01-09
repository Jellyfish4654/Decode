package org.firstinspires.ftc.teamcode.Framework.Auto;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.Framework.Params;

@Config
@Autonomous(name = "Blue Audience", preselectTeleOp = "JellyTele")
public class BlueAudienceAuto extends BaseOpMode {
    Pose2d scanPose;
    Pose2d shootPose;
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(true);
        Pose2d initialPose = new Pose2d(61.5, -23.5, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Params.alliance = Params.Alliance.BLUE;

        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓

        TrajectoryActionBuilder moveToScan;
        scanPose = null; //pos1

        TrajectoryActionBuilder moveToShoot;
        shootPose = null; //pos2

        TrajectoryActionBuilder moveToPark;

        // ↓ -------------- ↓ -------------- ↓ SHOOTING ACTIONS ↓ -------------- ↓ -------------- ↓
        SequentialAction swingPaddle = new SequentialAction(
                paddle.paddleUp(),
                new SleepAction(0.1),
                paddle.paddleDown()
        );

        SequentialAction shootGPP = new SequentialAction (
                new ParallelAction(
                        spindexer.greenOut(),
                        outtake.outtakeOnNear()
                ),
                swingPaddle,
                spindexer.purpleOut(),
                swingPaddle,
                spindexer.purpleOut(),
                swingPaddle,
                outtake.outtakeOff()
        );

        SequentialAction shootPGP = new SequentialAction (
                new ParallelAction(
                        spindexer.purpleOut(),
                        outtake.outtakeOnNear()
                ),
                swingPaddle,
                spindexer.greenOut(),
                swingPaddle,
                spindexer.purpleOut(),
                swingPaddle,
                outtake.outtakeOff()
        );
        SequentialAction shootPPG = new SequentialAction (
                new ParallelAction(
                        spindexer.purpleOut(),
                        outtake.outtakeOnNear()
                ),
                swingPaddle,
                spindexer.purpleOut(),
                swingPaddle,
                spindexer.greenOut(),
                swingPaddle,
                outtake.outtakeOff()
        );

        // ↓ -------------- ↓ -------------- ↓ AUTO ↓ -------------- ↓ -------------- ↓

        waitForStart();
        if (!isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction()
        );
        switch (Params.motif) {
            case GPP:
                Actions.runBlocking(
                        shootGPP
                );
            case PGP:
                Actions.runBlocking(
                        shootPGP
                );
            case PPG:
                Actions.runBlocking(
                        shootPPG
                );
        }

    }

    // ↓ -------------- ↓ -------------- ↓ VISION ACTIONS ↓ -------------- ↓ -------------- ↓
    public class ScanMotif implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            Params.Motif motif = vision.getObeliskMotif();
            return motif == Params.Motif.GPP || motif == Params.Motif.PGP || motif == Params.Motif.PPG;
        }
    }
    public Action scanMotif() { return new ScanMotif(); }
}