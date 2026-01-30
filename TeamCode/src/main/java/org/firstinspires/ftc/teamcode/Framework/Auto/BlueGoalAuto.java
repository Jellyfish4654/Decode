package org.firstinspires.ftc.teamcode.Framework.Auto;

import static org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive.PARAMS;

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
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.CameraThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.ThreeDeadWheelLocalizer;
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
    // ↓ -------------- ↓ -------------- ↓ POSITIONS TO CHANGE IN FTC DASH ↓ -------------- ↓ -------------- ↓
    // the doubles are the difference between the pose and shooting pose
    double collectFirstX = 1.2;
    double collectFirstY = -17.89;
    double collectFirstHeading = 44;
    double artifact1X = 1.1;
    double artifact1Y = -20.74;
    double artifact1Heading = 44.5;
    double artifact2X = 1.1;
    double artifact2Y = -25.44;
    double artifact2Heading = 44;
    double artifact3X = 0.6;
    double artifact3Y = -30.24;
    double artifact3Heading = 43;
    double collectSecondX = 23.5;
    double collectSecondY = -30.91;
    double collectSecondHeading = 42.5;
    double artifact4X = 23.6;
    double artifact4Y = -21.84;
    double artifact4Heading = 42.7;
    double artifact5X = 23.3;
    double artifact5Y = -26.64;
    double artifact5Heading = 42.3;
    double artifact6X = 22.9;
    double artifact6Y = -32.44;
    double artifact6Heading = 41.8;

    ThreeDeadWheelLocalizer camLocalizer;
    Pose2d currentCamPose = new Pose2d(0, 0, 0);
    boolean enablePoseCorrection = false; //enable this if the shootpose somehow keeps getting off
    double offsetX = 0;
    double offsetY = 0;
    double offsetHeading = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        // ↓ -------------- ↓ -------------- ↓ POSES ↓ -------------- ↓ -------------- ↓
        Pose2d initialPose = new Pose2d(-52, -49, Math.toRadians(232));
        scanPose = new Pose2d(-17.9, -15.4, Math.toRadians(158)); //pos1
        shootPose = new Pose2d (-13.5, -13.76, Math.toRadians(224)); //pos2
        collectFirstPose = new Pose2d(shootPose.position.x + collectFirstX, shootPose.position.y + collectFirstY, shootPose.heading.toDouble() + Math.toRadians(collectFirstHeading));
        artifactPose1 = new Pose2d(shootPose.position.x + artifact1X, shootPose.position.y + artifact1Y, shootPose.heading.toDouble() + Math.toRadians(artifact1Heading));
        artifactPose2 = new Pose2d(shootPose.position.x + artifact2X, shootPose.position.y + artifact2Y, shootPose.heading.toDouble() + Math.toRadians(artifact2Heading));
        artifactPose3 = new Pose2d(shootPose.position.x + artifact3X, shootPose.position.y + artifact3Y, shootPose.heading.toDouble() + Math.toRadians(artifact3Heading));
        collectSecondPose = new Pose2d(shootPose.position.x + collectSecondX, shootPose.position.y + collectSecondY, shootPose.heading.toDouble() + Math.toRadians(collectSecondHeading));
        artifactPose4 = new Pose2d(shootPose.position.x + artifact4X, shootPose.position.y + artifact4Y, shootPose.heading.toDouble() + Math.toRadians(artifact4Heading));
        artifactPose5 = new Pose2d(shootPose.position.x + artifact5X, shootPose.position.y + artifact5Y, shootPose.heading.toDouble() + Math.toRadians(artifact5Heading));
        artifactPose6 = new Pose2d(shootPose.position.x + artifact6X, shootPose.position.y + artifact6Y, shootPose.heading.toDouble() + Math.toRadians(artifact6Heading));
        // ↓ -------------- ↓ -------------- ↓ INITIALIZATION ↓ -------------- ↓ -------------- ↓
        initHardware(true);
        //TODO: SEE IF THIS BREAKS EVERYTHING
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        camLocalizer = new CameraThreeDeadWheelLocalizer(hardwareMap, PARAMS.inPerTick, initialPose, vision);
        currentCamPose = initialPose;
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
                .splineToLinearHeading(shootPose,Math.toRadians(260));

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
                    new ParallelAction(
                            correctPoses(),
                            new ShootMotif()
                    ),
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
                    new ParallelAction(
                            correctPoses(),
                            new ShootMotif()
                    ),
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
                    new ParallelAction(
                            correctPoses(),
                            new ShootMotif()
                    ),
                    //get out of shooting zone
                    drive.actionBuilder(shootPose).strafeTo(new Vector2d(-25,-45)).build()
            )
        );
    }

    public class CorrectPoses implements Action {
        @Override
        public boolean run (@NonNull TelemetryPacket packet) {
            camLocalizer.update();
            Pose2d currentPose = camLocalizer.getPose();
            if (currentPose != null) {
                if (enablePoseCorrection) {
                    offsetX = shootPose.position.x - currentPose.position.x;
                    offsetY = shootPose.position.y - currentPose.position.y;
                    offsetHeading = Math.toDegrees(shootPose.heading.toDouble() - currentPose.heading.toDouble());
                } else {
                    shootPose = currentPose;
                }
                collectFirstPose = new Pose2d(shootPose.position.x + collectFirstX + offsetX, shootPose.position.y + collectFirstY + offsetY, Math.toRadians(collectFirstHeading + offsetHeading));
                artifactPose1 = new Pose2d(shootPose.position.x + artifact1X + offsetX, shootPose.position.y + artifact1Y + offsetY, Math.toRadians(artifact1Heading + offsetHeading));
                artifactPose2 = new Pose2d(shootPose.position.x + artifact2X + offsetX, shootPose.position.y + artifact2Y + offsetY, Math.toRadians(artifact2Heading + offsetHeading));
                artifactPose3 = new Pose2d(shootPose.position.x + artifact3X + offsetX, shootPose.position.y + artifact3Y + offsetY, Math.toRadians(artifact3Heading + offsetHeading));
                collectSecondPose = new Pose2d(shootPose.position.x + collectSecondX + offsetX, shootPose.position.y + collectSecondY + offsetY, Math.toRadians(collectSecondHeading + offsetHeading));
                artifactPose4 = new Pose2d(shootPose.position.x + artifact4X + offsetX, shootPose.position.y + artifact4Y + offsetY, Math.toRadians(artifact4Heading + offsetHeading));
                artifactPose5 = new Pose2d(shootPose.position.x + artifact5X + offsetX, shootPose.position.y + artifact5Y + offsetY, Math.toRadians(artifact5Heading + offsetHeading));
                artifactPose6 = new Pose2d(shootPose.position.x + artifact6X + offsetX, shootPose.position.y + artifact6Y + offsetY, Math.toRadians(artifact6Heading + offsetHeading));
            }
            return false;
        }
    }

    public Action correctPoses() { return new CorrectPoses(); }


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