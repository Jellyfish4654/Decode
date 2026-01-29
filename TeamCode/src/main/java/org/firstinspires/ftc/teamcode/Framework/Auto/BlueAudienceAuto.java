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

import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.CameraMecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.Auto.RoadRunner.MecanumDrive;
import org.firstinspires.ftc.teamcode.Framework.Params;
import org.firstinspires.ftc.teamcode.JellyTele;

@Config
@Autonomous(name = "Blue Audience", preselectTeleOp = "JellyTele")
public class BlueAudienceAuto extends BaseAuto {
    Pose2d scanPose;
    Pose2d shootPose;
    @Override
    public void runOpMode() throws InterruptedException {


        // ↓ -------------- ↓ -------------- ↓ POSES ↓ -------------- ↓ -------------- ↓
        Pose2d initialPose = new Pose2d(61.5, -23.5, Math.toRadians(180));

        Pose2d preshootPose = new Pose2d(new Vector2d(-20,-20),Math.toRadians(-140));
        Pose2d cornerOnePose = new Pose2d(new Vector2d(54,-58), Math.toRadians(-70));
        Pose2d cornerTwoPose = new Pose2d(new Vector2d(60,-58), Math.toRadians(-90));
        Pose2d shootOnePose = new Pose2d(new Vector2d(-20,-20),Math.toRadians(-140));
        Pose2d collectArtifact1 = new Pose2d(35.5,-35,Math.toRadians(-90));
        Pose2d collectArtifact2 = new Pose2d(35.5,-40,Math.toRadians(-90));
        Pose2d collectArtifact3 = new Pose2d(35.5,-45,Math.toRadians(-90));
        Pose2d shootTwoPose = shootOnePose;

        // ↓ -------------- ↓ -------------- ↓ INITIALIZATION ↓ -------------- ↓ -------------- ↓
        initHardware(true);
        //TODO: SEE IF THIS BREAKS EVERYTHING
        CameraMecanumDrive drive = new CameraMecanumDrive(hardwareMap, initialPose, vision);
        Params.alliance = Params.Alliance.BLUE;

        // ↓ -------------- ↓ -------------- ↓ TRAJECTORIES ↓ -------------- ↓ -------------- ↓
        TrajectoryActionBuilder preshoot = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(preshootPose.position,preshootPose.heading);


        TrajectoryActionBuilder cornerOne = drive.actionBuilder(preshootPose)
                .strafeToLinearHeading(cornerOnePose.position, cornerOnePose.heading, intakeMovementConstraint);


        TrajectoryActionBuilder cornerTwo = drive.actionBuilder(cornerOnePose)
                .splineToConstantHeading(cornerTwoPose.position, cornerTwoPose.heading, intakeMovementConstraint);


        TrajectoryActionBuilder shootOne = drive.actionBuilder(cornerTwoPose)
                .strafeToLinearHeading(shootOnePose.position,shootOnePose.heading);


        SequentialAction motifOneCollector = new SequentialAction(
                new ParallelAction(
                        new SequentialAction(
                                spindexer.slotIn(),
                                new SleepAction(JellyTele.SPINDEXER_DELAY),
                                spindexer.contentsSet(Params.Artifact.GREEN)
                        ),

                        intake.intakeOn(),
                        drive.actionBuilder(shootOnePose)
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
                .strafeToLinearHeading(shootTwoPose.position,shootTwoPose.heading);

        TrajectoryActionBuilder park = drive.actionBuilder(shootTwoPose)
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




}