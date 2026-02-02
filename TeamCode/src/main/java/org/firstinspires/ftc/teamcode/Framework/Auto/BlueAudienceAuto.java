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

@Config
@Autonomous(name = "Blue Audience", preselectTeleOp = "JellyTele")
public class BlueAudienceAuto extends BaseAuto {
    Pose2d scanPose;
    Pose2d shootPose;
    Pose2d collectCornerPose;
    Pose2d collectCorner1Pose;
    Pose2d collectCorner2Pose;
    Pose2d collectArtifactPose;
    Pose2d collectArtifact1Pose;
    Pose2d collectArtifact2Pose;
    Pose2d collectArtifact3Pose;

    // ↓ -------------- ↓ -------------- ↓ POSITIONS TO CHANGE IN FTC DASH ↓ -------------- ↓ -------------- ↓
    public static double[] shoot = {56, -12.5, -140};
    // the doubles are the difference between the pose and shooting pose
    public static double[] scan = {-16, -11, -40};
    public static double[] corner = {1.2, -17.89, 50};
    public static double[] corner1 = {1.1, -20.74, 50.5};
    public static double[] corner2 = {1.1, -25.44, 50};
    public static double[] artifact = {23.5, -30.91, 96};
    public static double[] artifact1 = {23.5, -32, 96};
    public static double[] artifact2 = {23.6, -35, 96};
    public static double[] artifact3 = {23.3, -38, 96};

    ThreeDeadWheelLocalizer camLocalizer;
    public static boolean enablePoseCorrection = false; //enable this if the shootpose somehow keeps getting off
    double offsetX = 0;
    double offsetY = 0;
    double offsetHeading = 0;
    @Override
    public void runOpMode() throws InterruptedException {


        // ↓ -------------- ↓ -------------- ↓ POSES ↓ -------------- ↓ -------------- ↓
        Pose2d initialPose = new Pose2d(61.5, -23.5, Math.toRadians(180));
        //TODO: tune these poses using autopositionfinder
        shootPose = new Pose2d(new Vector2d(shoot[0],shoot[1]),Math.toRadians(shoot[2]));
        scanPose = new Pose2d(shootPose.position.x + scan[0], shootPose.position.y + scan[1], shootPose.heading.toDouble() + Math.toRadians(scan[2]));
        collectCornerPose = new Pose2d(shootPose.position.x + corner[0], shootPose.position.y + corner[1], shootPose.heading.toDouble() + Math.toRadians(corner[2]));
        collectCorner1Pose = new Pose2d(shootPose.position.x + corner1[0], shootPose.position.y + corner1[1], shootPose.heading.toDouble() + Math.toRadians(corner1[2]));
        collectCorner2Pose = new Pose2d(shootPose.position.x + corner2[0], shootPose.position.y + corner2[1], shootPose.heading.toDouble() + Math.toRadians(corner2[2]));
        collectArtifactPose = new Pose2d(shootPose.position.x + artifact[0], shootPose.position.y + artifact[1], shootPose.heading.toDouble() + Math.toRadians(artifact[2]));
        collectArtifact1Pose = new Pose2d(shootPose.position.x + artifact1[0], shootPose.position.y + artifact1[1], shootPose.heading.toDouble() + Math.toRadians(artifact1[2]));
        collectArtifact2Pose = new Pose2d(shootPose.position.x + artifact2[0], shootPose.position.y + artifact2[1], shootPose.heading.toDouble() + Math.toRadians(artifact2[2]));
        collectArtifact3Pose = new Pose2d(shootPose.position.x + artifact3[0], shootPose.position.y + artifact3[1], shootPose.heading.toDouble() + Math.toRadians(artifact3[2]));

        // ↓ -------------- ↓ -------------- ↓ INITIALIZATION ↓ -------------- ↓ -------------- ↓
        initHardware(true);
        //TODO: SEE IF THIS BREAKS EVERYTHING
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        camLocalizer = new CameraThreeDeadWheelLocalizer(hardwareMap, PARAMS.inPerTick, initialPose, vision);
        Params.alliance = Params.Alliance.BLUE;

        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓
        TrajectoryActionBuilder scanMotif = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(scanPose.position,scanPose.heading);
        TrajectoryActionBuilder preshoot = drive.actionBuilder(scanPose)
                .strafeToLinearHeading(shootPose.position,shootPose.heading);
        
        TrajectoryActionBuilder collectArtifact = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(collectArtifactPose.position,collectArtifactPose.heading);

        TrajectoryActionBuilder collectArtifact1 = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(collectArtifact1Pose.position,collectArtifact1Pose.heading);
        
        TrajectoryActionBuilder collectArtifact2 = drive.actionBuilder(collectArtifact1Pose)
                .strafeToLinearHeading(collectArtifact2Pose.position,collectArtifact2Pose.heading);
        
        TrajectoryActionBuilder collectArtifact3 = drive.actionBuilder(collectArtifact2Pose)
                .strafeToLinearHeading(collectArtifact3Pose.position,collectArtifact3Pose.heading);
        
        TrajectoryActionBuilder shootOne = drive.actionBuilder(collectArtifact3Pose)
                .strafeToLinearHeading(shootPose.position,shootPose.heading);


        SequentialAction motifOneCollector = new SequentialAction(
                collectArtifact.build(),
                new SleepAction(0.1),
                intake.intakeOn(),
                outtake.outtakeOnFar(), // ------- outtake prespin
                collectArtifact1.build(),
                new SleepAction(0.3),
                intake.intakeOff(),
                spindexer.contentsSet(Params.Artifact.GREEN),
                new ParallelAction(
                        spindexer.slotIn(),
                        new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                ),
                intake.intakeOn(),
                collectArtifact2.build(),
                new SleepAction(0.3),
                intake.intakeOff(),
                spindexer.contentsSet(Params.Artifact.PURPLE),
                new ParallelAction(
                        spindexer.slotIn(),
                        new SleepAction(JellyTele.SPINDEXER_DELAY /1000.0)
                ),
                intake.intakeOn(),
                collectArtifact3.build(),
                new SleepAction(0.3),
                intake.intakeOff(),
                spindexer.contentsSet(Params.Artifact.PURPLE),
                new SpindexerFirstOut()
        );

        TrajectoryActionBuilder park = drive.actionBuilder(shootPose)
                .lineToX(30);


        // ↓ -------------- ↓ -------------- ↓ AUTO ↓ -------------- ↓ -------------- ↓

        waitForStart();
        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        outtake.outtakeOnFar(),
                        scanMotif.build(),
                        scanMotif(),
                        new SpindexerFirstOut(),
                        preshoot.build(),
                        new SleepAction(0.5),
                        new ShootMotif(),
                        spindexer.slotIn(),
                        motifOneCollector,
                        outtake.outtakeOnFar(),
                        new SpindexerFirstOut(),
                        shootOne.build(),
                        new ShootMotif(),
                        outtake.outtakeOff(),
                        //motifOneCollector,
                        //new ShootMotif(),
                        park.build()
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
                collectCornerPose = new Pose2d(shootPose.position.x + corner[0] + offsetX, shootPose.position.y + corner[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(corner[2] + offsetHeading));
                collectCorner1Pose = new Pose2d(shootPose.position.x + corner1[0] + offsetX, shootPose.position.y + corner1[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(corner1[2] + offsetHeading));
                collectCorner2Pose = new Pose2d(shootPose.position.x + corner2[0] + offsetX, shootPose.position.y + corner2[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(corner2[2] + offsetHeading));
                collectArtifactPose = new Pose2d(shootPose.position.x + artifact[0] + offsetX, shootPose.position.y + artifact[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact[2] + offsetHeading));
                collectArtifact1Pose = new Pose2d(shootPose.position.x + artifact1[0] + offsetX, shootPose.position.y + artifact1[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact1[2] + offsetHeading));
                collectArtifact2Pose = new Pose2d(shootPose.position.x + artifact2[0] + offsetX, shootPose.position.y + artifact2[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact2[2] + offsetHeading));
                collectArtifact3Pose = new Pose2d(shootPose.position.x + artifact3[0] + offsetX, shootPose.position.y + artifact3[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact3[2] + offsetHeading));
            }
            return false;
        }
    }

    public Action correctPoses() { return new CorrectPoses(); }
}