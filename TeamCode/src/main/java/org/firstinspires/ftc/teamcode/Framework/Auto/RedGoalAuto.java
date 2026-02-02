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

import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.CameraThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.Framework.Params;
import org.firstinspires.ftc.teamcode.JellyTele;
import org.firstinspires.ftc.teamcode.Framework.Params.Artifact;

@Config
@Autonomous(name = "Red Goal", preselectTeleOp = "JellyTele")
public class RedGoalAuto extends BaseAuto {
    //poses
    private Pose2d scanPose;
    private Pose2d shootPose;
    private Pose2d gatePose;
    private Pose2d collectFirstPose;
    private Pose2d artifactPose1;
    private Pose2d artifactPose2;
    private Pose2d artifactPose3;
    private Pose2d collectSecondPose;
    private Pose2d artifactPose4;
    private Pose2d artifactPose5;
    private Pose2d artifactPose6;
    private Pose2d thirdPose;
    // ↓ -------------- ↓ -------------- ↓ POSITIONS TO CHANGE IN FTC DASH ↓ -------------- ↓ -------------- ↓
    public static double[] shoot = {-13.5, 13.76, -224};
    // the doubles are the difference between the pose and shooting pose
    public static double[] collectFirst = {1.2, 13, -45};
    public static double[] artifact1 = {1.1, 20.2, -45};
    public static double[] artifact2 = {1.1, 25, -45};
    public static double[] artifact3 = {0.6, 35, -45};
    public static double[] collectSecond = {27, 13, -47};
    public static double[] artifact4 = {27, 18.2, -47};
    public static double[] artifact5 = {27, 23, -47};
    public static double[] artifact6 = {27, 40, -47};

    ThreeDeadWheelLocalizer camLocalizer;
    public static boolean enablePoseCorrection = false; //enable this if the shootpose somehow keeps getting off
    double offsetX = 0;
    double offsetY = 0;
    double offsetHeading = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        // ↓ -------------- ↓ -------------- ↓ POSES ↓ -------------- ↓ -------------- ↓
        Pose2d initialPose = new Pose2d(-52, 49, Math.toRadians(-232));
        scanPose = new Pose2d(-17.9, 15.4, Math.toRadians(-158)); //pos1
        shootPose = new Pose2d (shoot[0], shoot[1], Math.toRadians(shoot[2])); //pos2
        collectFirstPose = new Pose2d(shootPose.position.x + collectFirst[0], shootPose.position.y + collectFirst[1], shootPose.heading.toDouble() + Math.toRadians(collectFirst[2]));
        artifactPose1 = new Pose2d(shootPose.position.x + artifact1[0], shootPose.position.y + artifact1[1], shootPose.heading.toDouble() + Math.toRadians(artifact1[2]));
        artifactPose2 = new Pose2d(shootPose.position.x + artifact2[0], shootPose.position.y + artifact2[1], shootPose.heading.toDouble() + Math.toRadians(artifact2[2]));
        artifactPose3 = new Pose2d(shootPose.position.x + artifact3[0], shootPose.position.y + artifact3[1], shootPose.heading.toDouble() + Math.toRadians(artifact3[2]));
        collectSecondPose = new Pose2d(shootPose.position.x + collectSecond[0], shootPose.position.y + collectSecond[1], shootPose.heading.toDouble() + Math.toRadians(collectSecond[2]));
        artifactPose4 = new Pose2d(shootPose.position.x + artifact4[0], shootPose.position.y + artifact4[1], shootPose.heading.toDouble() + Math.toRadians(artifact4[2]));
        artifactPose5 = new Pose2d(shootPose.position.x + artifact5[0], shootPose.position.y + artifact5[1], shootPose.heading.toDouble() + Math.toRadians(artifact5[2]));
        artifactPose6 = new Pose2d(shootPose.position.x + artifact6[0], shootPose.position.y + artifact6[1], shootPose.heading.toDouble() + Math.toRadians(artifact6[2]));
        // ↓ -------------- ↓ -------------- ↓ INITIALIZATION ↓ -------------- ↓ -------------- ↓
        initHardware(true);
        //TODO: SEE IF THIS BREAKS EVERYTHING
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        camLocalizer = new CameraThreeDeadWheelLocalizer(hardwareMap, PARAMS.inPerTick, initialPose, vision);
        Params.alliance = Params.Alliance.BLUE;
        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓
        TrajectoryActionBuilder moveToScan = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(scanPose.position, scanPose.heading);

        TrajectoryActionBuilder moveToShootPreload = drive.actionBuilder(scanPose)
                .strafeToLinearHeading(shootPose.position,shootPose.heading);

        TrajectoryActionBuilder moveToCollectFirst = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(collectFirstPose.position, collectFirstPose.heading);

        TrajectoryActionBuilder collectArtifact1 = drive.actionBuilder(collectFirstPose)
                .strafeToConstantHeading(artifactPose1.position, intakeMovementConstraint);

        TrajectoryActionBuilder collectArtifact2 = drive.actionBuilder(artifactPose1)
                .strafeToConstantHeading(artifactPose2.position, intakeMovementConstraint);

        TrajectoryActionBuilder collectArtifact3 = drive.actionBuilder(artifactPose2)
                .strafeToConstantHeading(artifactPose3.position, intakeMovementConstraint);

        gatePose = null; //pos4
        TrajectoryActionBuilder openGate; //ignore this unless we decide to go for 12 ball

        TrajectoryActionBuilder moveToShootFirst = drive.actionBuilder(artifactPose3)
                .strafeToLinearHeading(shootPose.position, shootPose.heading);

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
                        outtake.outtakeOnNear(),
                        moveToScan.build(),
                        outtake.outtakeOnNear(),
                        scanMotif(),
                        new SpindexerFirstOut(),
                        moveToShootPreload.build(),
                        new SleepAction(JellyTele.FLY_OUTTAKE_DELAY_SHORT /1000.0),
                        new ParallelAction(
                                correctPoses(),
                                new ShootMotif()
                        ),
                        new ParallelAction(
                                moveToCollectFirst.build(),
                                spindexer.slotIn()
                        ),
                        new SequentialAction(
                                intake.intakeOn(),
                                collectArtifact1.build(),
                                new SleepAction(0.3),
                                intake.intakeOff(),
                                spindexer.contentsSet(Artifact.PURPLE),
                                new ParallelAction(
                                        spindexer.slotIn(),
                                        new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                                ),
                                intake.intakeOn(),
                                collectArtifact2.build(),
                                new SleepAction(0.3),
                                intake.intakeOff(),
                                outtake.outtakeOnNear(), // ------- outtake prespin
                                spindexer.contentsSet(Artifact.PURPLE),
                                new ParallelAction(
                                        spindexer.slotIn(),
                                        new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                                ),
                                intake.intakeOn(),
                                collectArtifact3.build(),
                                new SleepAction(0.3),
                                intake.intakeOff(),
                                spindexer.contentsSet(Artifact.GREEN),
                                new SpindexerFirstOut()
                        ),
                        outtake.outtakeOnNear(),
                        moveToShootFirst.build(),
                        new ParallelAction(
                                correctPoses(),
                                new ShootMotif()
                        ),
                        new ParallelAction(
                                moveToCollectSecond.build(),
                                spindexer.slotIn()
                        ),
                        new SequentialAction(
                                intake.intakeOn(),
                                collectArtifact4.build(),
                                new SleepAction(0.3),
                                intake.intakeOff(),
                                spindexer.contentsSet(Artifact.PURPLE),
                                new ParallelAction(
                                        spindexer.slotIn(),
                                        new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                                ),
                                intake.intakeOn(),
                                collectArtifact5.build(),
                                new SleepAction(0.3),
                                intake.intakeOff(),
                                outtake.outtakeOnNear(), // ------- prespin
                                spindexer.contentsSet(Artifact.GREEN),
                                new ParallelAction(
                                        spindexer.slotIn(),
                                        new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                                ),
                                intake.intakeOn(),
                                collectArtifact6.build(),
                                new SleepAction(0.3),
                                intake.intakeOff(),
                                spindexer.contentsSet(Artifact.PURPLE),
                                new SpindexerFirstOut()
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
                collectFirstPose = new Pose2d(shootPose.position.x + collectFirst[0] + offsetX, shootPose.position.y + collectFirst[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(collectFirst[2] + offsetHeading));
                artifactPose1 = new Pose2d(shootPose.position.x + artifact1[0] + offsetX, shootPose.position.y + artifact1[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact1[2] + offsetHeading));
                artifactPose2 = new Pose2d(shootPose.position.x + artifact2[0] + offsetX, shootPose.position.y + artifact2[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact2[2] + offsetHeading));
                artifactPose3 = new Pose2d(shootPose.position.x + artifact3[0] + offsetX, shootPose.position.y + artifact3[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact3[2] + offsetHeading));
                collectSecondPose = new Pose2d(shootPose.position.x + collectSecond[0] + offsetX, shootPose.position.y + collectSecond[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(collectSecond[2] + offsetHeading));
                artifactPose4 = new Pose2d(shootPose.position.x + artifact4[0] + offsetX, shootPose.position.y + artifact4[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact4[2] + offsetHeading));
                artifactPose5 = new Pose2d(shootPose.position.x + artifact5[0] + offsetX, shootPose.position.y + artifact5[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact5[2] + offsetHeading));
                artifactPose6 = new Pose2d(shootPose.position.x + artifact6[0] + offsetX, shootPose.position.y + artifact6[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact6[2] + offsetHeading));
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
            double goalBearing = vision.getGoalBearing(Params.alliance, false);
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