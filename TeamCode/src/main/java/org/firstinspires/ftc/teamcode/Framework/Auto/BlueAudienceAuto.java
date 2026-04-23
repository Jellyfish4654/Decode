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
    Pose2d collectCorner;
    Pose2d collectCorner1;
    Pose2d collectCorner2;
    Pose2d collectArtifact;
    Pose2d collectArtifact1;
    Pose2d collectArtifact2;
    Pose2d collectArtifact3;

    // ↓ -------------- ↓ -------------- ↓ POSITIONS TO CHANGE IN FTC DASH ↓ -------------- ↓ -------------- ↓
    public static double[] shoot = {-13.5, -13.76, 224};
    // the doubles are the difference between the pose and shooting pose
    public static double[] corner = {1.2, -17.89, 44};
    public static double[] corner1 = {1.1, -20.74, 44.5};
    public static double[] corner2 = {1.1, -25.44, 44};
    public static double[] artifact = {23.5, -30.91, 42.5};
    public static double[] artifact1 = {0.6, -30.24, 43};
    public static double[] artifact2 = {23.6, -21.84, 42.7};
    public static double[] artifact3 = {23.3, -26.64, 42.3};

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
        shootPose = new Pose2d(new Vector2d(-20,-20),Math.toRadians(-140));
        collectCorner = new Pose2d(shootPose.position.x + corner[0], shootPose.position.y + corner[1], shootPose.heading.toDouble() + Math.toRadians(corner[2]));
        collectCorner1 = new Pose2d(shootPose.position.x + corner1[0], shootPose.position.y + corner1[1], shootPose.heading.toDouble() + Math.toRadians(corner1[2]));
        collectCorner2 = new Pose2d(shootPose.position.x + corner2[0], shootPose.position.y + corner2[1], shootPose.heading.toDouble() + Math.toRadians(corner2[2]));
        collectArtifact = new Pose2d(shootPose.position.x + artifact[0], shootPose.position.y + artifact[1], shootPose.heading.toDouble() + Math.toRadians(artifact[2]));
        collectArtifact1 = new Pose2d(shootPose.position.x + artifact1[0], shootPose.position.y + artifact1[1], shootPose.heading.toDouble() + Math.toRadians(artifact1[2]));
        collectArtifact2 = new Pose2d(shootPose.position.x + artifact2[0], shootPose.position.y + artifact2[1], shootPose.heading.toDouble() + Math.toRadians(artifact2[2]));
        collectArtifact3 = new Pose2d(shootPose.position.x + artifact3[0], shootPose.position.y + artifact3[1], shootPose.heading.toDouble() + Math.toRadians(artifact3[2]));

        // ↓ -------------- ↓ -------------- ↓ INITIALIZATION ↓ -------------- ↓ -------------- ↓
        initHardware(true);
        //TODO: SEE IF THIS BREAKS EVERYTHING
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);
        camLocalizer = new CameraThreeDeadWheelLocalizer(hardwareMap, PARAMS.inPerTick, initialPose, vision);
        Params.alliance = Params.Alliance.BLUE;

        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓
        TrajectoryActionBuilder preshoot = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(shootPose.position,shootPose.heading);


        TrajectoryActionBuilder cornerOne = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(collectCorner1.position, collectCorner1.heading, intakeMovementConstraint);


        TrajectoryActionBuilder cornerTwo = drive.actionBuilder(collectCorner1)
                .splineToConstantHeading(collectCorner2.position, collectCorner2.heading, intakeMovementConstraint);


        TrajectoryActionBuilder shootOne = drive.actionBuilder(collectCorner2)
                .strafeToLinearHeading(shootPose.position,shootPose.heading);


        SequentialAction motifOneCollector = new SequentialAction(
                new ParallelAction(
                        new SequentialAction(
                                spindexer.slotIn(),
                                new SleepAction(JellyTele.SPINDEXER_DELAY),
                                spindexer.contentsSet(Params.Artifact.GREEN)
                        ),

                        intake.intakeOn(),
                        drive.actionBuilder(shootPose)
                                .splineToLinearHeading(collectArtifact1,Math.toRadians(-90), intakeMovementConstraint)
                                .build()
                ),
                new ParallelAction(
                        new SequentialAction(
                                spindexer.slotIn(),
                                new SleepAction(JellyTele.SPINDEXER_DELAY),
                                spindexer.contentsSet(Params.Artifact.PURPLE)
                        ),
                        drive.actionBuilder(collectArtifact1)
                                .strafeToLinearHeading(collectArtifact2.position,collectArtifact2.heading, intakeMovementConstraint)
                                .build()
                ),
                new ParallelAction(
                        new SequentialAction(
                                spindexer.slotIn(),
                                new SleepAction(JellyTele.SPINDEXER_DELAY),
                                spindexer.contentsSet(Params.Artifact.PURPLE)
                        ),
                        drive.actionBuilder(collectArtifact2)
                                .lineToYConstantHeading(collectArtifact3.position.y, intakeMovementConstraint)
                                .build()
                ),
                intake.intakeOff()
        );

        TrajectoryActionBuilder shootTwo = drive.actionBuilder(collectArtifact3)
                .strafeToLinearHeading(shootPose.position,shootPose.heading);

        TrajectoryActionBuilder park = drive.actionBuilder(shootPose)
                .lineToX(30);


        // ↓ -------------- ↓ -------------- ↓ AUTO ↓ -------------- ↓ -------------- ↓

        waitForStart();
        if (isStopRequested()) return;
        Actions.runBlocking(
                new SequentialAction(
                        scanMotif(),
                        preshoot.build(),
                        new ShootMotif(),
                        new ParallelAction(
                                intake.intakeOn(),
                                spindexer.slotIn(),
                                cornerOne.build(),
                                spindexer.contentsSet(Params.Artifact.PURPLE)
                        ),
                        new ParallelAction(
                                spindexer.slotIn(),
                                cornerTwo.build(),
                                spindexer.contentsSet(Params.Artifact.GREEN)
                        ),
                        shootOne.build(),
                        new ParallelAction(
                                spindexer.purpleOut(),
                                outtake.outtakeOnFar(),
                                new SleepAction(JellyTele.FLY_OUTTAKE_DELAY_LONG /1000.0)
                        ),
                        swingPaddle(),
                        new ParallelAction(
                                spindexer.greenOut(),
                                outtake.outtakeOnFar(),
                                new SleepAction(JellyTele.FLY_OUTTAKE_DELAY_SHORT /1000.0)
                        ),
                        swingPaddle(),
                        outtake.outtakeOff(),
                        motifOneCollector,
                        shootTwo.build(),
                        new ShootMotif(),
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
                collectCorner = new Pose2d(shootPose.position.x + corner[0] + offsetX, shootPose.position.y + corner[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(corner[2] + offsetHeading));
                collectCorner1 = new Pose2d(shootPose.position.x + corner1[0] + offsetX, shootPose.position.y + corner1[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(corner1[2] + offsetHeading));
                collectCorner2 = new Pose2d(shootPose.position.x + corner2[0] + offsetX, shootPose.position.y + corner2[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(corner2[2] + offsetHeading));
                collectArtifact = new Pose2d(shootPose.position.x + artifact[0] + offsetX, shootPose.position.y + artifact[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact[2] + offsetHeading));
                collectArtifact1 = new Pose2d(shootPose.position.x + artifact1[0] + offsetX, shootPose.position.y + artifact1[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact1[2] + offsetHeading));
                collectArtifact2 = new Pose2d(shootPose.position.x + artifact2[0] + offsetX, shootPose.position.y + artifact2[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact2[2] + offsetHeading));
                collectArtifact3 = new Pose2d(shootPose.position.x + artifact3[0] + offsetX, shootPose.position.y + artifact3[1] + offsetY, shootPose.heading.toDouble() + Math.toRadians(artifact3[2] + offsetHeading));
            }
            return false;
        }
    }

    public Action correctPoses() { return new CorrectPoses(); }
}