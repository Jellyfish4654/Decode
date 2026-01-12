package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;

import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueLight;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedLight;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Vector;


public class JulianTest {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity aud = new DefaultBotBuilder(meepMeep)
                .setDimensions(18,18)
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(50, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        /*aud.runAction(aud.getDrive().actionBuilder(new Pose2d(-47, -47, Math.toRadians(-125)))
                        .strafeToLinearHeading(new Vector2d(-12,-12),Math.toRadians(-125))
                        .waitSeconds(1)
                        .strafeToLinearHeading(new Vector2d(-12,-30),Math.toRadians(-90))
                        .lineToY(-35)
                        .waitSeconds(0.5)
                        .lineToY(-40)
                        .waitSeconds(0.5)
                        .lineToY(-45)
                        .splineToLinearHeading(new Pose2d(new Vector2d(1.5,-51),Math.toRadians(-125)),Math.toRadians(70))
                        .splineToLinearHeading(new Pose2d(new Vector2d(-12,-19),Math.toRadians(-125)),Math.toRadians(-125))

                .build());*/
        aud.runAction(aud.getDrive().actionBuilder(new Pose2d(59, -12, Math.toRadians(180)))
                        .waitSeconds(3)
                .strafeToLinearHeading(new Vector2d(54,-58), Math.toRadians(-70))
                        .waitSeconds(0.5)
                .splineToConstantHeading(new Vector2d(60,-58), Math.toRadians(-90))
                        .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(60,-12),Math.toRadians(-140))
                        .waitSeconds(0.5)
                        .splineToLinearHeading(new Pose2d(35.5,-35,Math.toRadians(-90)),Math.toRadians(-90))
                        .strafeToLinearHeading(new Vector2d(35.5,-40),Math.toRadians(-90))
                .waitSeconds(0.5)
                        .lineToYConstantHeading(-45)
                .waitSeconds(0.5)
                        .strafeToLinearHeading(new Vector2d(60,-12),Math.toRadians(-140))
                        .lineToX(30)
                .build()
        );



        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(1f)
                .addEntity(aud)
                .start();
    }
}