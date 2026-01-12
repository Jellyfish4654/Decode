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
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.BaseOpMode;
import org.firstinspires.ftc.teamcode.Framework.Params;

@Config
@Autonomous(name = "Blue Goal", preselectTeleOp = "JellyTele")
public class BlueGoalAuto extends BaseOpMode {
    //poses
    Pose2d scanPose;
    Pose2d shootPose;
    Pose2d gatePose;
    Pose2d firstPose;
    Pose2d secondPose;
    Pose2d thirdPose;
    @Override
    public void runOpMode() throws InterruptedException {
        initHardware(true);
        Pose2d initialPose = new Pose2d(-61.5, -23.5, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Params.alliance = Params.Alliance.BLUE;

        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓

        TrajectoryActionBuilder moveToScan = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-47, -15), Math.toRadians(153.435));
        scanPose = new Pose2d(-47, -15, Math.toRadians(153.435)); //pos1

        TrajectoryActionBuilder moveToShootPreload = drive.actionBuilder(scanPose)
                .strafeToLinearHeading(new Vector2d(-23.5, -23.5), Math.toRadians(225));
        shootPose = new Pose2d (-23.5, -23.5, 225); //pos2

        TrajectoryActionBuilder collectFirst = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(new Vector2d(-11.5, -31), Math.toRadians(270))
                .lineToY(-43)
                .waitSeconds(1)
                .lineToY(-48)
                .waitSeconds(1)
                .lineToY(-53);
        firstPose = new Pose2d(-11.5, -53, Math.toRadians(270)); //pos3

        TrajectoryActionBuilder openGate; //ignore this unless we decide to go for 12 ball
        gatePose = null; //pos4

        TrajectoryActionBuilder moveToShootFirst = drive.actionBuilder(firstPose)
                .strafeToLinearHeading(new Vector2d(-23.5, -23.5), Math.toRadians(225));

        TrajectoryActionBuilder collectSecond = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(new Vector2d(12, -31), Math.toRadians(270))
                .lineToY(-43)
                .waitSeconds(1)
                .lineToY(-48)
                .waitSeconds(1)
                .lineToY(-53);
        secondPose = new Pose2d(12, -53, Math.toRadians(270)); //pos5

        TrajectoryActionBuilder moveToShootSecond = drive.actionBuilder(secondPose)
                .strafeToLinearHeading(new Vector2d(-23.5, -23.5), Math.toRadians(225));

        TrajectoryActionBuilder collectThird = drive.actionBuilder(shootPose) // ignore also unless doing 12 ball
                .strafeToLinearHeading(new Vector2d(35.5, -31), Math.toRadians(270))
                .lineToY(-43)
                .waitSeconds(1)
                .lineToY(-48)
                .waitSeconds(1)
                .lineToY(-53);
        thirdPose = new Pose2d(35.5, -53, Math.toRadians(270)); //pos6

        TrajectoryActionBuilder moveToShootThird = drive.actionBuilder(thirdPose) // ignore unless doing 12 ball
                .strafeToLinearHeading(new Vector2d(-23.5, -23.5), Math.toRadians(225));

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

        class ShootMotif implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
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
                return true;
            }
        }

        // ↓ -------------- ↓ -------------- ↓ AUTO ↓ -------------- ↓ -------------- ↓

        waitForStart();
        if (!isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        moveToScan.build(),
                        new ParallelAction(
                                scanMotif(),
                                moveToShootPreload.build()
                        ),
                        new ShootMotif(),
                        intake.intakeOn(),
                        new ParallelAction(
                                collectFirst.build(),
                                new SequentialAction(
                                        spindexer.slotIn(),
                                        spindexer.contentsSet(Params.Artifact.PURPLE),
                                        new SleepAction(0.75),
                                        spindexer.slotIn(),
                                        spindexer.contentsSet(Params.Artifact.PURPLE),
                                        new SleepAction(0.75),
                                        spindexer.slotIn(),
                                        spindexer.contentsSet(Params.Artifact.GREEN),
                                        new SleepAction(0.75),
                                        intake.intakeOff()
                                )
                        ),
                        moveToShootFirst.build(),
                        new ShootMotif(),
                        new ParallelAction(
                                collectSecond.build(),
                                new SequentialAction(
                                        spindexer.slotIn(),
                                        spindexer.contentsSet(Params.Artifact.PURPLE),
                                        new SleepAction(0.75),
                                        spindexer.slotIn(),
                                        spindexer.contentsSet(Params.Artifact.GREEN),
                                        new SleepAction(0.75),
                                        spindexer.slotIn(),
                                        spindexer.contentsSet(Params.Artifact.PURPLE),
                                        new SleepAction(0.75),
                                        intake.intakeOff()
                                )
                        ),

                        moveToShootSecond.build(),
                        new ShootMotif(),
                        //get out of shooting zone
                        drive.actionBuilder(secondPose).strafeTo(new Vector2d(-25,-45)).build()
                )
        );
    }


    public class VerifyPos implements Action {
        private int posNum;
        VerifyPos (int pos) {
            posNum = pos;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            double goalBearing = vision.getGoalBearing(Params.alliance);
            double goalDistance = vision.getGoalDistance(Params.alliance);
            double goalX = -58.5;
            double goalY = -54.5;
            double goalDirection = 223;
            double webcamX = 0; //measure this
            double webcamY = 0; //measure this
            double xToGoal = 0;
            double yToGoal = 0;

            double botHeading;
            double xPos = 0;
            double yPos = 0;
            if (goalBearing != 0 && goalDistance != 0) {
                if (goalBearing > Math.toRadians(360)) {
                    goalBearing -= Math.toRadians(360);
                }
                botHeading = Math.toRadians(goalDirection)+goalBearing;
                if (botHeading < Math.toRadians(goalDirection)) {
                    xToGoal = Math.sin(-goalBearing)*goalDistance;
                    yToGoal = Math.cos(-goalBearing)*goalDistance;
                } else if (botHeading > Math.toRadians(goalDirection)){
                    xToGoal = Math.cos(-goalBearing)*goalDistance;
                    yToGoal = Math.sin(-goalBearing)*goalDistance;
                }

                xPos = goalX-xToGoal-webcamX;
                yPos = goalY-yToGoal-webcamY;
                Pose2d calcPose = new Pose2d(xPos, yPos, botHeading);

                if (posNum == 1) {
                    scanPose = calcPose;
                } else if (posNum == 2) {
                    shootPose = calcPose;
                } else if (posNum == 3) {
                    firstPose = calcPose;
                } else if (posNum == 4) {
                    gatePose = calcPose;
                } else if (posNum == 5) {
                    secondPose = calcPose;
                } else if (posNum == 6) {
                    thirdPose = calcPose;
                }
            }
            return true;
        }
    }
    public Action verifyPos(int pos) { return new VerifyPos(pos); }
}