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

import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.CameraMecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.Params;
import org.firstinspires.ftc.teamcode.JellyTele;
import org.firstinspires.ftc.teamcode.Framework.Params.Artifact;

@Config
@Autonomous(name = "Blue Goal", preselectTeleOp = "JellyTele")
public class BlueGoalAuto extends BaseAuto {
    //poses
    Pose2d scanPose;
    Pose2d shootPose;
    Pose2d gatePose;
    Pose2d collectFirstPose;
    Pose2d artifactPose1;
    Pose2d artifactPose2;
    Pose2d artifactPose3;
    Pose2d collectSecondPose;
    Pose2d artifactPose4;
    Pose2d artifactPose5;
    Pose2d artifactPose6;
    Pose2d thirdPose;
    @Override
    public void runOpMode() throws InterruptedException {
        // ↓ -------------- ↓ -------------- ↓ POSES ↓ -------------- ↓ -------------- ↓
        Pose2d initialPose = new Pose2d(-52, -49, Math.toRadians(-128));
        scanPose = new Pose2d(-17.9, -15.4, Math.toRadians(158)); //pos1
        shootPose = new Pose2d (-13.5, -13.76, Math.toRadians(-136)); //pos2
        collectFirstPose = new Pose2d(-12.3, -31.65, Math.toRadians(-92)); //pos3
        artifactPose1 = new Pose2d(-12.4, -34.5, Math.toRadians(-91.5));
        artifactPose2 = new Pose2d(-12.4, -39.2, Math.toRadians(-92));
        artifactPose3 = new Pose2d(-12.9, -44, Math.toRadians(-93));
        collectSecondPose = new Pose2d(10, -44.67, Math.toRadians(-93.5)); //pos3
        artifactPose4 = new Pose2d(10.1, -35.6, Math.toRadians(-93.3));
        artifactPose5 = new Pose2d(9.8, -40.4, Math.toRadians(-93.7));
        artifactPose6 = new Pose2d(9.4, -46.2, Math.toRadians(-94.2));
        // ↓ -------------- ↓ -------------- ↓ INITIALIZATION ↓ -------------- ↓ -------------- ↓
        initHardware(true);
        //TODO: SEE IF THIS BREAKS EVERYTHING
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Params.alliance = Params.Alliance.BLUE;
        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓
        TrajectoryActionBuilder moveToScan = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(scanPose.position, scanPose.heading);

        TrajectoryActionBuilder moveToShootPreload = drive.actionBuilder(scanPose)
                .strafeToLinearHeading(shootPose.position,shootPose.heading);

        TrajectoryActionBuilder moveToCollectFirst = drive.actionBuilder(shootPose)
                .splineToLinearHeading(collectFirstPose,Math.toRadians(265));

        TrajectoryActionBuilder collectArtifact1 = drive.actionBuilder(collectFirstPose)
                .strafeToConstantHeading(artifactPose1.position, intakeMovementConstraint);

        TrajectoryActionBuilder collectArtifact2 = drive.actionBuilder(artifactPose1)
                .strafeToConstantHeading(artifactPose2.position, intakeMovementConstraint);

        TrajectoryActionBuilder collectArtifact3 = drive.actionBuilder(artifactPose2)
                .strafeToConstantHeading(artifactPose3.position, intakeMovementConstraint);

        gatePose = null; //pos4
        TrajectoryActionBuilder openGate; //ignore this unless we decide to go for 12 ball

        TrajectoryActionBuilder moveToShootFirst = drive.actionBuilder(artifactPose3)
                .splineToLinearHeading(shootPose,Math.toRadians(255));

        TrajectoryActionBuilder moveToCollectSecond = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(collectSecondPose.position, collectSecondPose.heading);

        TrajectoryActionBuilder collectArtifact4 = drive.actionBuilder(collectSecondPose)
                .strafeToConstantHeading(artifactPose4.position, intakeMovementConstraint);


        TrajectoryActionBuilder collectArtifact5 = drive.actionBuilder(artifactPose4)
                .strafeToConstantHeading(artifactPose5.position, intakeMovementConstraint);


        TrajectoryActionBuilder collectArtifact6 = drive.actionBuilder(artifactPose5)
                .strafeToConstantHeading(artifactPose6.position, intakeMovementConstraint);


        TrajectoryActionBuilder moveToShootSecond = drive.actionBuilder(artifactPose6)
                .strafeToLinearHeading(shootPose.position, shootPose.heading);

        //TODO: REFACTOR IF USING IF NOT THEN JUST LEAVE IT IN THE CODEBASE I GUESS LOL
        thirdPose = new Pose2d(35.5, -53, Math.toRadians(270)); //pos6
        TrajectoryActionBuilder collectThird = drive.actionBuilder(shootPose) // ignore also unless doing 12 ball
                .strafeToLinearHeading(new Vector2d(35.5, -31), Math.toRadians(270))
                .lineToY(-43)
                .waitSeconds(1)
                .lineToY(-48)
                .waitSeconds(1)
                .lineToY(-53);

        TrajectoryActionBuilder moveToShootThird = drive.actionBuilder(thirdPose) // ignore unless doing 12 ball
                .strafeToLinearHeading(new Vector2d(-23.5, -23.5), Math.toRadians(225));
        

        // ↓ -------------- ↓ -------------- ↓ AUTO ↓ -------------- ↓ -------------- ↓

        waitForStart();
        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                    moveToScan.build(),
                    scanMotif(),
                    outtake.outtakeOnNear(),
                    moveToShootPreload.build(),
                    new ShootMotif(),
                    new ParallelAction(
                            moveToCollectFirst.build(),
                            intake.intakeOn(),
                            spindexer.slotIn()
                    ),
                    new SequentialAction(
                            collectArtifact1.build(),
                            intake.intakeOff(),
                            new SleepAction(0.4),
                            spindexer.contentsSet(Artifact.PURPLE),
                            new ParallelAction(
                                    intake.intakeOn(),
                                    spindexer.slotIn(),
                                    new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                            ),
                            collectArtifact2.build(),
                            intake.intakeOff(),
                            new SleepAction(0.4),
                            spindexer.contentsSet(Artifact.PURPLE),
                            new ParallelAction(
                                    intake.intakeOn(),
                                    spindexer.slotIn(),
                                    new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                            ),
                            collectArtifact3.build(),
                            intake.intakeOff(),
                            new SleepAction(0.4),
                            spindexer.contentsSet(Artifact.GREEN)
                    ),
                    outtake.outtakeOnNear(),
                    moveToShootFirst.build(),
                    new ShootMotif(),
                    new ParallelAction(
                            moveToCollectSecond.build(),
                            intake.intakeOn(),
                            spindexer.slotIn()
                    ),
                    new SequentialAction(
                            collectArtifact4.build(),
                            spindexer.contentsSet(Artifact.PURPLE),
                            new ParallelAction(
                                    spindexer.slotIn(),
                                    new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                            ),
                            collectArtifact5.build(),
                            spindexer.contentsSet(Artifact.GREEN),
                            new ParallelAction(
                                    spindexer.slotIn(),
                                    new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                            ),
                            collectArtifact6.build(),
                            spindexer.contentsSet(Artifact.PURPLE),
                            intake.intakeOff()
                    ),
                    outtake.outtakeOnNear(),
                    moveToShootSecond.build(),
                    new ShootMotif(),
                    //get out of shooting zone
                    drive.actionBuilder(shootPose).strafeTo(new Vector2d(-25,-45)).build()
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
                    collectFirstPose = calcPose;
                } else if (posNum == 4) {
                    gatePose = calcPose;
                } else if (posNum == 5) {
                    collectSecondPose = calcPose;
                } else if (posNum == 6) {
                    thirdPose = calcPose;
                }
            }
            return false;
        }
    }
    public Action verifyPos(int pos) { return new VerifyPos(pos); }
}