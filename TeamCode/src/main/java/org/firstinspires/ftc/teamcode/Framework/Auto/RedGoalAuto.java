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
import org.firstinspires.ftc.teamcode.Framework.Params;
import org.firstinspires.ftc.teamcode.JellyTele;
<<<<<<< Updated upstream
        =======
import org.firstinspires.ftc.teamcode.Framework.Params.Artifact;
>>>>>>> Stashed changes

@Config
@Autonomous(name = "Red Goal", preselectTeleOp = "JellyTele")
public class RedGoalAuto extends BaseAuto {
    //poses
    Pose2d scanPose;
    Pose2d shootPose;
    Pose2d gatePose;
    Pose2d firstPose;
    Pose2d secondPose;
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
        initHardware(true);
        Pose2d initialPose = new Pose2d(-61.5, 33, Math.toRadians(180));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        Params.alliance = Params.Alliance.BLUE;
        Params.alliance = Params.Alliance.RED;

        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓

        TrajectoryActionBuilder moveToScan = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-47, 15), Math.toRadians(206.565));
        scanPose = new Pose2d(-47, 15, Math.toRadians(206.565)); //pos1

        TrajectoryActionBuilder moveToShootPreload = drive.actionBuilder(scanPose)
                .strafeToLinearHeading(new Vector2d(-23.5, 23.5), Math.toRadians(135));
        shootPose = new Pose2d (-23.5, 23.5, Math.toRadians(135)); //pos2

<<<<<<< Updated upstream
        TrajectoryActionBuilder collectFirst = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(new Vector2d(-11.5, 27), Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(-11.5,32))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(-11.5,37))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(-11.5,42));
        firstPose = new Pose2d(-11.5, -53, Math.toRadians(90)); //pos3
=======
        TrajectoryActionBuilder moveToCollectFirst = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(new Vector2d(-13, 29), Math.toRadians(95));
        collectFirstPose = new Pose2d(-13, 29, Math.toRadians(95)); //pos3

        TrajectoryActionBuilder collectArtifact1 = drive.actionBuilder(collectFirstPose)
                .strafeToConstantHeading(new Vector2d(-13,34));

        artifactPose1 = new Pose2d(-13, 34, Math.toRadians(95));

        TrajectoryActionBuilder collectArtifact2 = drive.actionBuilder(artifactPose1)
                .strafeToConstantHeading(new Vector2d(-13,39));

        artifactPose2 = new Pose2d(-13, 39, Math.toRadians(95));

        TrajectoryActionBuilder collectArtifact3 = drive.actionBuilder(artifactPose2)
                .strafeToConstantHeading(new Vector2d(-13,44));

        artifactPose3 = new Pose2d(-13, 44, Math.toRadians(95));
>>>>>>> Stashed changes

        TrajectoryActionBuilder openGate; //ignore this unless we decide to go for 12 ball
        gatePose = null; //pos4

        TrajectoryActionBuilder moveToShootFirst = drive.actionBuilder(firstPose)
        TrajectoryActionBuilder moveToShootFirst = drive.actionBuilder(artifactPose3)
                .strafeToLinearHeading(new Vector2d(-23.5, 23.5), Math.toRadians(135));

<<<<<<< Updated upstream
        TrajectoryActionBuilder collectSecond = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(new Vector2d(12, 27), Math.toRadians(90))
                .strafeToConstantHeading(new Vector2d(12,32))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(12,37))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(12,42));
        secondPose = new Pose2d(12, -53, Math.toRadians(90)); //pos5
=======
        TrajectoryActionBuilder moveToCollectSecond = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(new Vector2d(9, 29), Math.toRadians(100));
        collectFirstPose = new Pose2d(9, 29, Math.toRadians(100)); //pos3
>>>>>>> Stashed changes

        TrajectoryActionBuilder collectArtifact4 = drive.actionBuilder(collectFirstPose)
                .strafeToConstantHeading(new Vector2d(9,34));

        artifactPose4 = new Pose2d(9, 34, Math.toRadians(100));

        TrajectoryActionBuilder collectArtifact5 = drive.actionBuilder(artifactPose1)
                .strafeToConstantHeading(new Vector2d(9,39));

        artifactPose5 = new Pose2d(9, 39, Math.toRadians(100));

        TrajectoryActionBuilder moveToShootSecond = drive.actionBuilder(secondPose)
        TrajectoryActionBuilder collectArtifact6 = drive.actionBuilder(artifactPose2)
                .strafeToConstantHeading(new Vector2d(9,44));

        artifactPose6 = new Pose2d(9, 44, Math.toRadians(100));

        TrajectoryActionBuilder moveToShootSecond = drive.actionBuilder(artifactPose6)
                .strafeToLinearHeading(new Vector2d(-23.5, 23.5), Math.toRadians(135));

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


        // ↓ -------------- ↓ -------------- ↓ AUTO ↓ -------------- ↓ -------------- ↓

        waitForStart();
        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        moveToScan.build(),
                        scanMotif(),
                        moveToShootPreload.build(),
                        new ShootMotif(),
                        intake.intakeOn(),
                        new ParallelAction(
                                collectFirst.build(),
                                new SequentialAction(
                                        moveToCollectFirst.build(),
                                        intake.intakeOn(),
                                        spindexer.slotIn()
                                ),
                                new SequentialAction(
                                        collectArtifact1.build(),
                                        spindexer.contentsSet(Artifact.PURPLE),
                                        new ParallelAction(
                                                spindexer.slotIn(),
                                                <<<<<<< Updated upstream
                                        spindexer.contentsSet(Params.Artifact.PURPLE),
                                        new SleepAction(1),
                                        spindexer.slotIn(),
                                        spindexer.contentsSet(Params.Artifact.PURPLE),
                                        new SleepAction(1),
                                        spindexer.slotIn(),
                                        spindexer.contentsSet(Params.Artifact.GREEN),
                                        new SleepAction(1),
                                        intake.intakeOff()
                                )
                                        =======
                                        new SleepAction(JellyTele.SPIN_INTAKE_DELAY/1000)
                        ),
                        collectArtifact2.build(),
                        spindexer.contentsSet(Artifact.PURPLE),
                        new ParallelAction(
                                spindexer.slotIn(),
                                new SleepAction(JellyTele.SPIN_INTAKE_DELAY/1000)
                        ),
                        collectArtifact3.build(),
                        spindexer.contentsSet(Artifact.GREEN),
                        intake.intakeOff()
                                >>>>>>> Stashed changes
                ),
                moveToShootFirst.build(),
                new ShootMotif(),
                intake.intakeOn(),
                new ParallelAction(
                        <<<<<<< Updated upstream
                collectSecond.build(),
                new SequentialAction(
                        spindexer.slotIn(),
                        spindexer.contentsSet(Params.Artifact.PURPLE),
                        new SleepAction(1),
                        spindexer.slotIn(),
                        spindexer.contentsSet(Params.Artifact.GREEN),
                        new SleepAction(1),
                        spindexer.slotIn(),
                        spindexer.contentsSet(Params.Artifact.PURPLE),
                        new SleepAction(1),
                        intake.intakeOff()
                )
                        =======
                        moveToCollectSecond.build(),
                intake.intakeOn(),
                spindexer.slotIn()
        ),
                new SequentialAction(
                        collectArtifact4.build(),
                        spindexer.contentsSet(Artifact.PURPLE),
                        new ParallelAction(
                                spindexer.slotIn(),
                                new SleepAction(JellyTele.SPIN_INTAKE_DELAY/1000)
                        ),
                        collectArtifact5.build(),
                        spindexer.contentsSet(Artifact.GREEN),
                        new ParallelAction(
                                spindexer.slotIn(),
                                new SleepAction(JellyTele.SPIN_INTAKE_DELAY/1000)
                        ),
                        collectArtifact6.build(),
                        spindexer.contentsSet(Artifact.PURPLE),
                        intake.intakeOff()
                                >>>>>>> Stashed changes
                ),

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
                    firstPose = calcPose;
                    collectFirstPose = calcPose;
                } else if (posNum == 4) {
                    gatePose = calcPose;
                } else if (posNum == 5) {
                    secondPose = calcPose;
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