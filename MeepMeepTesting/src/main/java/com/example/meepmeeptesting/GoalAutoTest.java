package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;

import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;


public class GoalAutoTest {
    static Pose2d scanPose;
    static Pose2d shootPose;
    static Pose2d gatePose;
    static Pose2d firstPose;
    static Pose2d secondPose;
    static Pose2d thirdPose;

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(500);

        RoadRunnerBotEntity aud = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeRedLight())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(18,18)
                .build();

        DriveShim drive = aud.getDrive();
        Pose2d initialPose = new Pose2d(-51, -48, Math.toRadians(-127));

        TrajectoryActionBuilder moveToScan = drive.actionBuilder(initialPose)
                .strafeToLinearHeading(new Vector2d(-47, -15), Math.toRadians(153.435));
        scanPose = new Pose2d(-47, -15, Math.toRadians(153.435)); //pos1

        TrajectoryActionBuilder moveToShootPreload = drive.actionBuilder(scanPose)
                .strafeToLinearHeading(new Vector2d(-23.5, -23.5), Math.toRadians(225));
        shootPose = new Pose2d (-23.5, -23.5, Math.toRadians(225)); //pos2

        TrajectoryActionBuilder collectFirst = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(new Vector2d(-11.5, -29), Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(-11.5,-34))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(-11.5,-39))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(-11.5,-44));
        firstPose = new Pose2d(-11.5, -44, Math.toRadians(270)); //pos3

        TrajectoryActionBuilder openGate; //ignore this unless we decide to go for 12 ball
        gatePose = null; //pos4

        TrajectoryActionBuilder moveToShootFirst = drive.actionBuilder(firstPose)
                .strafeToLinearHeading(new Vector2d(-23.5, -23.5), Math.toRadians(225));

        TrajectoryActionBuilder collectSecond = drive.actionBuilder(shootPose)
                .strafeToLinearHeading(new Vector2d(12, -29), Math.toRadians(270))
                .strafeToConstantHeading(new Vector2d(12,-34))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(12,-39))
                .waitSeconds(1)
                .strafeToConstantHeading(new Vector2d(12,-44));
        secondPose = new Pose2d(12, -44, Math.toRadians(270)); //pos5

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



        aud.runAction(
                new SequentialAction(
                        moveToScan.build(),
                        moveToShootPreload.build(),
                        new ParallelAction(
                                collectFirst.build(),
                                new SleepAction(0.75*3)
                        ),
                        moveToShootFirst.build(),
                        new ParallelAction(
                                collectSecond.build(),
                                new SleepAction(0.75*3)
                        ),

                        moveToShootSecond.build(),
                        //get out of shooting zone
                        drive.actionBuilder(shootPose).strafeTo(new Vector2d(-25,-45)).build()
                )
        );


        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(1f)
                .addEntity(aud)
                .start();
    }
}